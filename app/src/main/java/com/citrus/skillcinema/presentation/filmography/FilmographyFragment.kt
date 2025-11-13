package com.citrus.skillcinema.presentation.filmography

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.citrus.skillcinema.R
import com.citrus.skillcinema.data.models.ActorFilm
import com.citrus.skillcinema.databinding.FragmentFilmographyBinding
import com.citrus.skillcinema.presentation.adapters.filmography.FilmographyAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmographyFragment : Fragment() {

    private var _binding: FragmentFilmographyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilmographyViewModel by viewModels()
    private lateinit var filmographyAdapter: FilmographyAdapter
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmographyBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val args: FilmographyFragmentArgs by navArgs()
        val personId = args.personId

        navController = findNavController()


        binding.filmographyRecyclerView.layoutManager = LinearLayoutManager(context)
        filmographyAdapter = FilmographyAdapter(navController)
        binding.filmographyRecyclerView.adapter = filmographyAdapter


        viewModel.loadInfo(personId)
        binding.progressBarFilmography.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBarFilmography.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.filmographyRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }

        viewModel.actorInfo.onEach { person ->
            binding.filmographyTextView.text = person?.nameRu
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        setChipTitle(
            viewModel.asActorFilms,
            binding.actorChipFilmography,
            titleMan = R.string.actor,
            titleWoman = R.string.actress,
            viewModel.isItMan
        )
        setChipTitle(
            viewModel.asDirectorFilms,
            binding.directorChipFilmography,
            titleMan = R.string.director,
            titleWoman = R.string.director,
            viewModel.isItMan
        )
        setChipTitle(
            viewModel.asHimselfFilms,
            binding.himselfChipFilmography,
            titleMan = R.string.himself_man,
            titleWoman = R.string.himself_woman,
            viewModel.isItMan
        )
        setChipTitle(
            viewModel.asProducerFilms,
            binding.producerChipFilmography,
            titleMan = R.string.producer,
            titleWoman = R.string.producer,
            viewModel.isItMan
        )
        setChipTitle(
            viewModel.asWriterFilms,
            binding.writerChipFilmography,
            titleMan = R.string.writer,
            titleWoman = R.string.writer,
            viewModel.isItMan
        )
        setChipTitle(
            viewModel.otherFilms,
            binding.otherChipFilmography,
            titleMan = R.string.other,
            titleWoman = R.string.other,
            viewModel.isItMan
        )

        binding.actorChipFilmography.isChecked = true

        viewModel.asActorFilms.onEach {
            filmographyAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.filmographyChipGroup.setOnCheckedStateChangeListener { group, _ ->
            when (group.checkedChipId) {
                R.id.actor_chip_filmography -> {
                    viewModel.asActorFilms.onEach {
                        filmographyAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.director_chip_filmography -> {
                    viewModel.asDirectorFilms.onEach {
                        filmographyAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.producer_chip_filmography -> {
                    viewModel.asProducerFilms.onEach {
                        filmographyAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.himself_chip_filmography -> {
                    viewModel.asHimselfFilms.onEach {
                        filmographyAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.writer_chip_filmography -> {
                    viewModel.asWriterFilms.onEach {
                        filmographyAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.other_chip_filmography -> {
                    viewModel.otherFilms.onEach {
                        filmographyAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
            }


        }


        binding.filmogrBackButton.setOnClickListener {
            navController.popBackStack()
        }

    }


    private fun setChipTitle(
        stateFlowFilms: StateFlow<List<ActorFilm>>,
        chip: Chip,
        titleMan: Int,
        titleWoman: Int,
        isItManStateFlow: StateFlow<Boolean>
    ) {
        stateFlowFilms.onEach { filmList ->
            if (filmList.isEmpty()) {
                chip.visibility = View.GONE
            } else {
                with(chip) {
                    visibility = View.VISIBLE
                    isItManStateFlow.onEach { isMan ->
                        val title = if (isMan) {
                            getString(titleMan, filmList.size)
                        } else {
                            getString(titleWoman, filmList.size)
                        }
                        text = title
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*  private fun setupPagination(){
          binding.filmographyRecyclerView.addOnScrollListener(object  : RecyclerView.OnScrollListener(){
              override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                  super.onScrolled(recyclerView, dx, dy)
                  val layoutManager = recyclerView.layoutManager as GridLayoutManager
                  val totalItemCount = layoutManager.itemCount
                  val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                  if (!isLoading && totalItemCount <= (lastVisibleItem + 5)){
                      isLoading = true
                      loadMoreMovies
                  }
              }
          }
      }

      private fun loadMoreMovies()*/

}