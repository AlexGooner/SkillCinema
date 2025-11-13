package com.citrus.skillcinema.presentation.actorpage

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.citrus.skillcinema.databinding.FragmentActorPageBinding
import com.citrus.skillcinema.presentation.adapters.actorpage.ActorPageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActorPageFragment : Fragment() {

    private var _binding: FragmentActorPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActorPageViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var actorFilmographyAdapter: ActorPageAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorPageBinding.inflate(inflater)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ActorPageFragmentArgs by navArgs()
        val personId = args.personId


        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        navController = findNavController()

        actorFilmographyAdapter = ActorPageAdapter(navController)

        binding.actorPageRecyclerView.adapter = actorFilmographyAdapter

        binding.progressBar.visibility = View.VISIBLE

        viewModel.loadInfo(personId)

        binding.actorBackButton.setOnClickListener {
            navController.popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.actorPageRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actorInfo.collect { actor ->
                actor?.let {
                    with(binding) {
                        if(actor.nameRu == ""){
                            actorPageNameTextView.text = actor.nameEn
                        }
                        else {
                            actorPageNameTextView.text = actor.nameRu
                        }
                        actorProfessionTextView.text = actor.profession
                        Glide.with(actorCover.context)
                            .load(actor.posterUrl)
                            .into(actorCover)
                        bestTextView.text = "Лучшее"
                        actorPageFilmographyTextView.text = "Фильмография"
                        actorPageFilmographyAllTextView.text = "К списку"
                        viewModel!!.loadMoviesByActorFilms(it.films)
                    }
                    binding.actorPageFilmographyAllTextView.setOnClickListener {
                        val action = ActorPageFragmentDirections.actionNavigationActorPageToNavigationFilmography(personId)
                        navController.navigate(action)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.topMovies.collect{ topMovies ->
                actorFilmographyAdapter = ActorPageAdapter(navController)
                binding.actorPageRecyclerView.adapter = actorFilmographyAdapter
                val sortedList = topMovies.take(10)
                actorFilmographyAdapter.submitList(sortedList)
                if (topMovies.isEmpty()){
                    binding.bestTextView.visibility = View.GONE
                } else {
                    binding.bestTextView.visibility = View.VISIBLE
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movies.collect { movies ->
                val movieText = getMoviesText(movies.size)
                binding.filmographyCountTextView.text = movieText
            }
        }


    }

    private fun getMoviesText(count : Int): String{
        return when{
            count % 10 == 1 && 100 != 11 -> "$count фильм"
            count % 10 in 2..4 && (count % 100 !in 12..14) -> "$count фильма"
            else -> "$count фильмов"
        }
    }
}