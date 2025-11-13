package com.citrus.skillcinema.presentation.searchpage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.citrus.skillcinema.databinding.FragmentSearchBinding
import com.citrus.skillcinema.presentation.adapters.search.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        navController = findNavController()

        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context)
        searchAdapter = SearchAdapter(navController)
        binding.searchRecyclerView.adapter = searchAdapter

        val args: SearchFragmentArgs by navArgs()
        val country = args.country
        val genre = args.genre
        val yearFrom = args.yearFrom
        val yearTo = args.yearTo
        val minRating = args.minRating
        val maxRating = args.maxRating
        val type = args.type
        val order = args.order
        val isWatched = args.isWatched


        val countryValue = countryMap[country] ?: 1
        val countryList = listOf(countryValue)
        val genreValue = genreMap[genre] ?: 1
        val genreList = listOf(genreValue)

        binding.progressBarSearch.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBarSearch.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.searchRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getSearch(country = countryList, genreList, type, order, minRating,
                maxRating, yearFrom, yearTo, 1, isWatched)
            viewModel.searchedFilms.collect{films ->
                searchAdapter.submitList(films)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val watchedFilmIds = viewModel.getWatchedFilmIds()
            searchAdapter.setWatchedFilmIds(watchedFilmIds)
        }


        binding.appCompatEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim() ?: ""
                viewModel.setSearchQuery(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        binding.settingsButton.setOnClickListener {
            val action = SearchFragmentDirections.navigationSearchToSettings(defaultCountry, defaultGenre, yearFrom, yearTo)
            navController.navigate(action)
        }
    }
    companion object {
        const val defaultCountry = "Россия"
        const val defaultGenre = "Боевик"
        private val countryMap = mapOf(
            "США" to 1,
            "Швейцария" to 2,
            "Франция" to 3,
            "Польша" to 4,
            "Великобритания" to 5,
            "Швеция" to 6,
            "Индия" to 7,
            "Испания" to 8,
            "Германия" to 9,
            "Италия" to 10,
            "Гонконг" to 11,
            "Германия (ФРГ)" to 12,
            "Австралия" to 13,
            "Канада" to 14,
            "Мексика" to 15,
            "Россия" to 34
        )
        private val genreMap = mapOf(
          "Триллеры" to 1,
          "Драмы" to 2,
          "Криминал" to 3,
          "Мелодрамы" to 4,
          "Детективы" to 5,
          "Фантастика" to 6,
          "Приключения" to 7,
          "Биографии" to 8,
          "Нуары" to 9,
           "Вестерны" to 10,
           "Боевики" to 11,
           "Фэнтези" to 12,
           "Комедии" to 13,
           "Военные фильмы" to 14,
           "Исторические фильмы" to 15,
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}