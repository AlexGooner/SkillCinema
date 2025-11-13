package com.citrus.skillcinema.presentation.searchpage.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.citrus.skillcinema.databinding.FragmentCountryAndGenreBinding
import com.citrus.skillcinema.presentation.adapters.search.settings.CountryAndGenreAdapter

class CountryAndGenreFragment : Fragment() {

    private var _binding: FragmentCountryAndGenreBinding? = null
    private val binding get() = _binding!!
    private lateinit var countryOrGenreAdapter: CountryAndGenreAdapter
    private lateinit var navController: NavController
    private val viewModel: SearchSettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryAndGenreBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: CountryAndGenreFragmentArgs by navArgs()
        val type = args.countryOrGenre
        navController = findNavController()
        val ids: List<Int> = when (type) {
            "country" -> ((1..15).toList() + 34)
            "genre" -> (1..15).toList()
            else -> emptyList()
        }

        if (type == "genre"){
            binding.countryAndGenreTitle.text = "Жанр"
            binding.appCompatEditText.hint = "Введите жанр"
        }


        countryOrGenreAdapter = CountryAndGenreAdapter(ids, type) { selectedItem ->
            if (type == "country") {
                viewModel.country = selectedItem
                val action =
                    CountryAndGenreFragmentDirections.countryOrGenreToSettings(country = selectedItem)
                navController.navigate(action)
            } else {
                viewModel.genre = selectedItem
                val action =
                    CountryAndGenreFragmentDirections.countryOrGenreToSettings(genre = selectedItem)
                navController.navigate(action)
            }
        }
        binding.countryOrGenreRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.countryOrGenreRecyclerView.adapter = countryOrGenreAdapter


        binding.appCompatEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = p0.toString().trim()
                if (type == "country") {
                    countryOrGenreAdapter.filterCountries(query)
                } else {
                    countryOrGenreAdapter.filterGenres(query)
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.countryAndGenreBackBtn.setOnClickListener {
            navController.popBackStack()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}