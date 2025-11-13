package com.citrus.skillcinema.presentation.profilepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.citrus.skillcinema.databinding.FragmentProfileListBinding
import com.citrus.skillcinema.presentation.adapters.profilepage.ListSavedFilmsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


internal const val COLLECTION_NAME = "collection_name"

@AndroidEntryPoint
class ProfileListFragment : Fragment() {

    private var _binging: FragmentProfileListBinding? = null

    private val binding get() = _binging!!

    private val viewModel: ProfileViewModel by activityViewModels()

    private var collectionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            collectionName = it.getString(COLLECTION_NAME)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binging = FragmentProfileListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileListTextView.text = collectionName

        val filmsAdapter = ListSavedFilmsAdapter()
        binding.recyclerView.adapter = filmsAdapter

        lifecycleScope.launch {
            viewModel.collections.collect { collectionList ->
                collectionList.forEach { filmCollection ->
                    if (filmCollection.savedCollection.collectionName == collectionName) {
                        filmsAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }
                        filmsAdapter.submitList(filmCollection.collectionFilms)
                    }
                }
            }
        }

        binding.profileListBackButton.setOnClickListener {
            val navController : NavController = findNavController()
            navController.popBackStack()

        }
    }

    private fun moveToFilmPage(id: Int) {
        val action = ProfileListFragmentDirections.actionNavigationProfileListToCertain(id)
        val navController : NavController = findNavController()
        navController.navigate(action)
    }

}