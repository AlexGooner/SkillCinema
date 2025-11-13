package com.citrus.skillcinema.presentation.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.citrus.skillcinema.R
import com.citrus.skillcinema.data.models.MovieData
import com.citrus.skillcinema.databinding.FragmentHomeBinding
import com.citrus.skillcinema.presentation.adapters.homepage.MovieListAdapter
import com.citrus.skillcinema.presentation.adapters.homepage.MovieListRandomCountryAndGenreAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment(), OnShowAllClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var navController: NavController

    private lateinit var movieListRandomAdapter : MovieListRandomCountryAndGenreAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.VISIBLE

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        navController = findNavController()

        binding.progressBarMovies.visibility = View.VISIBLE


        movieListRandomAdapter =
            MovieListRandomCountryAndGenreAdapter(navController, this, viewModel)
        val movieListPopularAdapter = MovieListAdapter(navController, "TOP_POPULAR_MOVIES")
        val movieListPremierAdapter = MovieListAdapter(navController, "PREMIERS")
        val movieListTopAdapter = MovieListAdapter(navController, "TOP_250_MOVIES")
        val movieListSeriesAdapter = MovieListAdapter(navController, "TOP_250_TV_SHOWS")

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBarMovies.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.customViewPremier.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.customViewTop.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.customViewPopular.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.customViewSeries.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.customViewRandom.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }

        binding.customViewPremier.setRecyclerViewAdapter(movieListPremierAdapter)
        binding.customViewPremier.setLeftText(getString(R.string.premiers))
        binding.customViewPremier.setOnRightTextClickListener{
            movieListPremierAdapter.click()
        }


        binding.customViewTop.setRecyclerViewAdapter(movieListTopAdapter)
        binding.customViewTop.setLeftText(getString(R.string.top))
        binding.customViewTop.setOnRightTextClickListener{
            movieListTopAdapter.click()
        }

        binding.customViewPopular.setRecyclerViewAdapter(movieListPopularAdapter)
        binding.customViewPopular.setLeftText(getString(R.string.popular))
        binding.customViewPopular.setOnRightTextClickListener{
            movieListPopularAdapter.click()
        }

        binding.customViewSeries.setRecyclerViewAdapter(movieListSeriesAdapter)
        binding.customViewSeries.setLeftText(getString(R.string.series))
        binding.customViewSeries.setOnRightTextClickListener{
            movieListSeriesAdapter.click()
        }

        binding.customViewRandom.setRecyclerViewAdapter(movieListRandomAdapter)
        binding.customViewRandom.setOnRightTextClickListener{
            movieListRandomAdapter.clickRandomAll()
        }





        viewModel.premierMovies.onEach {
            val sortedMoviesList = it.take(20)
            movieListPremierAdapter.submitList(sortedMoviesList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.topMovies.onEach { movies ->
            movieListTopAdapter.submitList(movies)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.popularMovies.onEach {
            movieListPopularAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.series.onEach {
            movieListSeriesAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.randomCAndG.onEach { movies ->
            movieListRandomAdapter.submitList(movies)
            updateRandomCountryAndGenreText()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.currentCountry.onEach {
            updateRandomCountryAndGenreText()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.currentGenre.onEach {
            updateRandomCountryAndGenreText()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            val watchedFilmIds = viewModel.getWatchedFilmIds()
            movieListPremierAdapter.setWatchedFilmIds(watchedFilmIds)
            movieListTopAdapter.setWatchedFilmIds(watchedFilmIds)
            movieListSeriesAdapter.setWatchedFilmIds(watchedFilmIds)
            movieListPopularAdapter.setWatchedFilmIds(watchedFilmIds)
            movieListRandomAdapter.setWatchedFilmIds(watchedFilmIds)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateRandomCountryAndGenreText() {
        val countryText = MovieData.getCountryName(viewModel.currentCountry.value)
        val genreText = MovieData.getGenreName(viewModel.currentGenre.value)
        binding.customViewRandom.setLeftText("$genreText $countryText")
    }

    override fun onShowAllClick() {
        val bundle = Bundle().apply {
            putString("movieType", "RANDOM")
        }
        navController.navigate(R.id.action_navigation_home_to_navigation_all_movies, bundle)    }


}