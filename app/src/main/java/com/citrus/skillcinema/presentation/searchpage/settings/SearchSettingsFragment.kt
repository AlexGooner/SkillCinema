package com.citrus.skillcinema.presentation.searchpage.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.citrus.skillcinema.databinding.FragmentSearchSettingsBinding

class SearchSettingsFragment : Fragment() {

    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchSettingsViewModel by activityViewModels()
    private lateinit var navController: NavController
    private var type: String = "ALL"
    private var order: String = "RATING"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSettingsBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        navController = findNavController()

        val args: SearchSettingsFragmentArgs by navArgs()
        val country = args.country
        val genre = args.genre
        val yearFrom = args.yearFrom
        val yearTo = args.yearTo

        binding.settingsCountryTextView.text = viewModel.country ?: country
        binding.settingsGenreTextView.text = viewModel.genre ?: genre
        binding.settingsYearTextView.text =
            "C ${viewModel.yearFrom ?: yearFrom} по ${viewModel.yearTo ?: yearTo}"


        binding.settingsCountryTextView.setOnClickListener {
            val action =
                SearchSettingsFragmentDirections.navigationSettingsToCountryOrGenre("country")
            navController.navigate(action)
        }


        binding.settingsGenreTextView.setOnClickListener {
            val action =
                SearchSettingsFragmentDirections.navigationSettingsToCountryOrGenre("genre")
            navController.navigate(action)
        }


        binding.settingsYearTextView.setOnClickListener {
            val action = SearchSettingsFragmentDirections.navigationSettingsToYear()
            navController.navigate(action)
        }

        binding.rangeSlider.addOnChangeListener { slider, value, fromUser ->
            val values = slider.values
            val minRating = values[0].toInt().toString()
            viewModel.ratingFrom = values[0].toInt()
            val maxRating = values[1].toInt().toString()
            viewModel.ratingTo = values[1].toInt()
            binding.minValueText.text = minRating
            binding.maxValueText.text = maxRating
        }

        binding.rangeSlider.values = listOf(viewModel.ratingFrom?.toFloat(),
            viewModel.ratingTo?.toFloat()
        )
        binding.minValueText.text = viewModel.ratingFrom.toString()
        binding.maxValueText.text = viewModel.ratingTo.toString()

        when (viewModel.selectedType) {
            "ALL" -> binding.chipSettingsAll.isChecked = true
            "FILM" -> binding.chipSettingsMovies.isChecked = true
            "TV_SERIES" -> binding.chipSettingsSeries.isChecked = true
        }

        when (viewModel.selectedOrder) {
            "RATING" -> binding.chipSettingsRating.isChecked = true
            "NUM_VOTE" -> binding.chipSettingsPopularity.isChecked = true
            "YEAR" -> binding.chipSettingsDate.isChecked = true
        }



        binding.chipSettingsAll.setOnClickListener {
            selectChipType("all")
            if (binding.chipSettingsAll.isChecked) {
                binding.chipSettingsMovies.isChecked = false
                binding.chipSettingsSeries.isChecked = false
            }
        }

        binding.chipSettingsMovies.setOnClickListener {
            selectChipType("films")
            if (binding.chipSettingsMovies.isChecked) {
                binding.chipSettingsAll.isChecked = false
                binding.chipSettingsSeries.isChecked = false
            }
        }

        binding.chipSettingsSeries.setOnClickListener {
            selectChipType("series")
            if (binding.chipSettingsSeries.isChecked) {
                binding.chipSettingsAll.isChecked = false
                binding.chipSettingsMovies.isChecked = false
            }
        }


        binding.chipSettingsDate.setOnClickListener {
            selectChipOrder("year")
            if (binding.chipSettingsDate.isChecked) {
                binding.chipSettingsPopularity.isChecked = false
                binding.chipSettingsRating.isChecked = false
            }
        }
        binding.chipSettingsPopularity.setOnClickListener {
            selectChipOrder("numVote")
            if (binding.chipSettingsPopularity.isChecked) {
                binding.chipSettingsDate.isChecked = false
                binding.chipSettingsRating.isChecked = false
            }
        }
        binding.chipSettingsRating.setOnClickListener {
            selectChipOrder("rating")
            if (binding.chipSettingsRating.isChecked) {
                binding.chipSettingsPopularity.isChecked = false
                binding.chipSettingsDate.isChecked = false
            }
        }

        binding.acceptBtn.setOnClickListener {
            val minRating = binding.minValueText.text.toString().toInt()
            val maxRating = binding.maxValueText.text.toString().toInt()

            val action = SearchSettingsFragmentDirections.navigationSettingsToSearch(
                country = viewModel.country ?: country,
                genre = viewModel.genre ?: genre,
                yearFrom = viewModel.yearFrom?: yearFrom,
                yearTo = viewModel.yearTo ?: yearTo,
                minRating,
                maxRating,
                type,
                order,
                isWatched = binding.checkBoxWatched.isChecked
            )
            navController.navigate(action)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun selectChipType(selectedType: String) {
        when (selectedType) {
            "all" -> {
                type = "ALL"
                viewModel.selectedType = "ALL"
            }

            "films" -> {
                type = "FILM"
                viewModel.selectedType = "FILM"
            }

            "series" -> {
                type = "TV_SERIES"
                viewModel.selectedType = "TV_SERIES"
            }
        }
    }

    private fun selectChipOrder(selectedOrder: String) {
        when (selectedOrder) {
            "rating" -> {
                order = "RATING"
                viewModel.selectedOrder = "RATING"
            }

            "numVote" -> {
                order = "NUM_VOTE"
                viewModel.selectedOrder = "NUM_VOTE"
            }

            "year" -> {
                order = "YEAR"
                viewModel.selectedOrder = "YEAR"
            }

        }
    }


}