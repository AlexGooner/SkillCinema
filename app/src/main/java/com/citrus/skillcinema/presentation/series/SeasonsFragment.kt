package com.citrus.skillcinema.presentation.series

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
import com.citrus.skillcinema.databinding.FragmentSeasonsBinding
import com.citrus.skillcinema.presentation.adapters.series.SeasonsAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeasonsFragment : Fragment() {

    private var _binding: FragmentSeasonsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SeasonsViewModel by viewModels()
    private lateinit var seasonsAdapter: SeasonsAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeasonsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val args: SeasonsFragmentArgs by navArgs()

        val movieId = args.movieId

        navController = findNavController()

        binding.progressBarSeasons.visibility = View.VISIBLE

        binding.seasonsRecyclerView.layoutManager = LinearLayoutManager(context)
        seasonsAdapter = SeasonsAdapter(navController)
        binding.seasonsRecyclerView.adapter = seasonsAdapter

        viewModel.loadInfo(movieId)
        viewModel.loadMovieInfo(movieId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBarSeasons.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.seasonsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movie.collect { movie ->
                movie?.let {
                    binding.seasonsTitleTextView.text = it.nameRu ?: it.nameOriginal
                }
            }
        }

        viewModel.seasonsInfo.onEach { seasonInfo ->
            val chipCount = seasonInfo?.total
            val startNum = seasonInfo?.items?.get(0)?.number ?: 0

            with(binding.seasonsChipGroup){
                addChip(chipCount, startNum)
                setOnCheckedStateChangeListener{group, _ ->
                    val chip = view.findViewById<Chip>(group.checkedChipId)
                    val checkedSeason = seasonInfo?.items?.get(chip.id)
                    val countOfSeasonsEpisodesText = resources.getQuantityString(
                        R.plurals.count_of_seasons_seasonsPage,
                        checkedSeason?.episodes?.size ?: 0,
                        checkedSeason?.number,
                        checkedSeason?.episodes?.size
                    )
                    seasonsAdapter.submitList(checkedSeason?.episodes)
                    binding.seasonCountTextView.text = countOfSeasonsEpisodesText
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.seasonsBackButton.setOnClickListener {
            val navigation = findNavController()
            navigation.popBackStack()
        }
    }


    private fun ChipGroup.addChip(count: Int?, startNum: Int){
        if (count != null){
            val endPoint = if (startNum == 0) count - 1 else count

            for (i in startNum..endPoint){
                val chip = layoutInflater.inflate(
                    R.layout.layout_chip,
                    binding.seasonsChipGroup,
                    false
                ) as Chip
                chip.id = if (startNum == 0) i else i-1
                chip.text = getString(R.string.chip_text_season, i)
                chip.isClickable = true
                addView(chip)
            }
        }
    }

}