package com.citrus.skillcinema.presentation.allmovies

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.citrus.skillcinema.R
import com.citrus.skillcinema.data.models.MovieData
import com.citrus.skillcinema.databinding.FragmentAllMoviesBinding
import com.citrus.skillcinema.presentation.adapters.allmovies.AllMoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllMoviesFragment : Fragment() {

    private var _binding: FragmentAllMoviesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AllMoviesViewModel by viewModels()
    private lateinit var allMoviesAdapter: AllMoviesAdapter
    private lateinit var navController: NavController

    private var currentPage = 1
    private var isLoading = false


    private var movieType: String? = null
    private var year: Int? = null
    private var month: String? = null
    private var genre: Int? = null
    private var country: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllMoviesBinding.inflate(inflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        navController = findNavController()
        allMoviesAdapter = AllMoviesAdapter(navController)

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = allMoviesAdapter

        val args = arguments
        movieType = args?.getString("movieType")
        year = args?.getInt("year")
        month = args?.getString("month")
        genre = args?.getInt("genre")
        country = args?.getInt("country")
        binding.progressBarAllMovies.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBarAllMovies.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }

        loadMovies()


        viewModel.movies.observe(viewLifecycleOwner) {movies ->
            if (movies != null) {
                allMoviesAdapter.setMovies(movies)
            }
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!isLoading && !recyclerView.canScrollVertically(1)){
                    currentPage++
                    setupPagination()
                }
            }
        })

        binding.allMoviesBackBtn.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }


    }

    @SuppressLint("SetTextI18n")
    private fun loadMovies() {
        when (movieType) {
            "PREMIERS" -> {
                year?.let { year ->
                    month?.let { month ->
                        viewModel.loadPremiers(year, month)
                        binding.allMoviesTitle.text = getString(R.string.premiers)
                    }
                }
            }
            "RANDOM" -> {
                country?.let {
                    genre?.let {
                        viewModel.loadRandomMovies(it, country!!)
                        binding.allMoviesTitle.text = MovieData.getGenreName(it) + " " + MovieData.getCountryName(country!!)
                    }
                }
            }
            "TOP_250_MOVIES" -> {
                movieType?.let {
                    val title = getString(R.string.top)
                    viewModel.loadTopMovies(it, title)
                    binding.allMoviesTitle.text = title
                }
            }
            "TOP_250_TV_SHOWS" -> {
                movieType?.let {
                    val title = getString(R.string.series)
                    viewModel.loadSeries(it, title)
                    binding.allMoviesTitle.text = title
                }
            }
            "TOP_POPULAR_MOVIES" -> {
                movieType?.let {
                    val title = getString(R.string.popular)
                    viewModel.loadPopular(it, title)
                    binding.allMoviesTitle.text = title
                }
            }
        }
    }

    private fun setupPagination() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= (lastVisibleItem + 5)) {
                    isLoading = true
                    loadMoreMovies(movieType)
                }
            }
        })
    }

    private fun loadMoreMovies(type: String?) {
        viewModel.loadMoreMovies(type!!, currentPage).onEach { newMovies ->
            allMoviesAdapter.setMovies(allMoviesAdapter.movieList + newMovies)
            currentPage++
            isLoading = false
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}