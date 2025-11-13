package com.citrus.skillcinema.presentation.profilepage

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.citrus.skillcinema.R
import com.citrus.skillcinema.databinding.FragmentProfileBinding
import com.citrus.skillcinema.presentation.adapters.profilepage.CollectionCardAdapter
import com.citrus.skillcinema.presentation.adapters.profilepage.ProfileInterestingAdapter
import com.citrus.skillcinema.presentation.adapters.profilepage.ProfileWatchedAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var profileWatchedAdapter: ProfileWatchedAdapter
    private lateinit var collectionAdapter: CollectionCardAdapter
    private lateinit var profileInterestingAdapter: ProfileInterestingAdapter


    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        navController = findNavController()


        profileInterestingAdapter = ProfileInterestingAdapter(
            navController
        ) { onDeleteInterestedClick() }
        binding.wasInterestedListProfile.setRecyclerViewAdapter(profileInterestingAdapter)

        collectionAdapter = CollectionCardAdapter()
        binding.collectionsRecyclerView.adapter = collectionAdapter


        binding.wasWatchedListProfile.setLeftText(getString(R.string.watched))
        binding.wasInterestedListProfile.setLeftText(getString(R.string.interesting))


        binding.addCollectionTextView.setOnClickListener {
            addCollectionDialog()
        }

        profileWatchedAdapter = ProfileWatchedAdapter(
            navController = navController,
            onDeleteAllClicked = { onDeleteWatchedClick() })
        binding.wasWatchedListProfile.setRecyclerViewAdapter(profileWatchedAdapter)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.watchedMovies.collect { movies ->
                profileWatchedAdapter.submitList(movies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.collections.collect { collectionList ->
                collectionAdapter.submitList(collectionList)
                collectionAdapter.onItemClick =
                    { collectionName -> moveToListPage(collectionName) }
                collectionAdapter.onCloseClick =
                    { collectionName -> viewModel.deleteCollection(collectionName) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadWatchedFilms()
            viewModel.getInterestedFilms()
            viewModel.loadDefaultCollections()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.likedMovies.collect { movies ->
                collectionAdapter.updateFilmsCount(movies.size)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.interestedMovies.collect { movies ->
                profileInterestingAdapter.submitList(movies)
            }
        }

    }


    private fun moveToListPage(category: String) {
        val bundle = Bundle().apply {
            putString(COLLECTION_NAME, category)
        }
        findNavController().navigate(R.id.action_navigation_profile_to_profile_list, args = bundle)
    }

    private fun errorBottomDialog() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_error_message)
        dialog.setCancelable(true)
        dialog.dismissWithAnimation
        val closeButton = dialog.findViewById<AppCompatImageView>(R.id.close_error)
        closeButton?.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
    }

    private fun addCollectionDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_collection)
        dialog.setCancelable(false)
        val editText = dialog.findViewById<AppCompatEditText>(R.id.edittext_dialog_profile)
        val doneButton = dialog.findViewById<MaterialButton>(R.id.done_dialog_profile)
        val closeButton = dialog.findViewById<AppCompatImageView>(R.id.close_error)
        closeButton?.setOnClickListener {
            dialog.cancel()
        }
        doneButton.setOnClickListener {
            val collectionName = editText.text.toString()
            viewModel.addCollection(collectionName)
            viewModel.collectionAdded.onEach {
                if (it == collectionName) {
                    errorBottomDialog()
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            dialog.cancel()
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onDeleteWatchedClick() {
        viewModel.deleteWatchedFilms()
        binding.wasWatchedListProfile.animate()
            .alpha(0f)
            .setDuration(1000)
            .withEndAction {
                binding.wasWatchedListProfile.visibility =
                    View.GONE
            }
    }

    private fun onDeleteInterestedClick() {
        viewModel.deleteInterestedFilms()
        binding.wasInterestedListProfile.animate()
            .alpha(0f)
            .setDuration(1000)
            .withEndAction {
                binding.wasInterestedListProfile.visibility =
                    View.GONE
            }
    }
}