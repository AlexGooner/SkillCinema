package com.citrus.skillcinema.presentation.searchpage.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.citrus.skillcinema.databinding.FragmentYearBinding
import com.citrus.skillcinema.presentation.adapters.search.settings.YearFromToAdapter

class YearFragment : Fragment() {

    private var _binding: FragmentYearBinding? = null
    private val binding get() = _binding!!

    private lateinit var yearFromAdapter: YearFromToAdapter
    private lateinit var yearToAdapter: YearFromToAdapter
    private var currentPageFrom = 0
    private var currentPageTo = 0
    private val itemsPerPage = 12
    private val startYear = 1970
    private val endYear = 2025
    val years = (startYear..endYear).toList()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentYearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        navController = findNavController()
        setupFirstRecyclerView()
        setupSecondRecyclerView()
        setupButtons()



        binding.chooseBtn.setOnClickListener {
            val selectedYearFrom = yearFromAdapter.getSelectedYear()
            val selectedYearTo = yearToAdapter.getSelectedYear()

            if (selectedYearFrom != null && selectedYearTo != null) {
                val action = YearFragmentDirections.yearToSettings(
                    yearFrom = selectedYearFrom,
                    yearTo = selectedYearTo
                )
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, выберите оба года", Toast.LENGTH_SHORT).show()
            }

        }

        binding.yearBackBtn.setOnClickListener {
            navController.popBackStack()
        }

    }


    private fun setupSecondRecyclerView() {

        updateRecyclerView(years, currentPageTo, binding.secondYearFromToTextView, false)
    }

    private fun setupFirstRecyclerView(){
        updateRecyclerView(years, currentPageFrom, binding.firstYearFromToTextView, true)

    }

    @SuppressLint("SetTextI18n")
    private fun updateRecyclerView(years: List<Int>, currentPage: Int, textView: TextView, isFrom: Boolean) {
        val startIndex = currentPage * itemsPerPage
        val endIndex = minOf(startIndex + itemsPerPage, years.size)

        if (startIndex < years.size) {
            val currentYears = years.subList(startIndex, endIndex)
            textView.text = "${currentYears.first()} - ${currentYears.last()}"

            val adapter = YearFromToAdapter(currentYears) { selectedYear ->
                Toast.makeText(requireContext(), "Выбранный год: $selectedYear", Toast.LENGTH_SHORT)
                    .show()
            }

            if (isFrom) {
                yearFromAdapter = adapter
                binding.yearFromRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.yearFromRecyclerView.adapter = yearFromAdapter
            } else {
                yearToAdapter = adapter
                binding.yearToRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.yearToRecyclerView.adapter = yearToAdapter
            }
        }
    }

    private fun setupButtons() {
        binding.firstYearChooseBackBtn.setOnClickListener {
            if (currentPageFrom > 0) {
                currentPageFrom--
                setupFirstRecyclerView()
            }
        }
        binding.firstYearChooseForwardBtn.setOnClickListener {
            if ((currentPageFrom + 1) * itemsPerPage < (endYear - startYear + 1)) {
                currentPageFrom++
                setupFirstRecyclerView()
            }
        }

        binding.secondYearChooseBackBtn.setOnClickListener {
            if (currentPageTo > 0) {
                currentPageTo--
                setupSecondRecyclerView()
            }
        }
        binding.secondYearChooseForwardBtn.setOnClickListener {
            if ((currentPageTo + 1) * itemsPerPage < (endYear - startYear + 1)) {
                currentPageTo++
                setupSecondRecyclerView()
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}