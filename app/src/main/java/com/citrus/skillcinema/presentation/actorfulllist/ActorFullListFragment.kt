package com.citrus.skillcinema.presentation.actorfulllist

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
import com.citrus.skillcinema.databinding.FragmentActorFullListBinding
import com.citrus.skillcinema.presentation.adapters.actorfullist.ActorFullListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActorFullListFragment : Fragment() {

    private var _binding: FragmentActorFullListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActorFullListViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var actorFullListAdapter: ActorFullListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorFullListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ActorFullListFragmentArgs by navArgs()
        val movieId = args.movieId
        val requiredProfessionKey = args.professionKey

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        navController = findNavController()

        actorFullListAdapter = ActorFullListAdapter(navController, requireContext())

        binding.progressBarActorFull.visibility = View.VISIBLE

        binding.actorFullListRecyclerView.adapter = actorFullListAdapter
        binding.actorFullListRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.loadList(movieId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBarActorFull.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.actorFullListRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stuffList.collect{ stuffList ->
                if (requiredProfessionKey == "ACTOR"){
                    val filteredActorList = stuffList.filter { it.professionKey == "ACTOR" }
                    actorFullListAdapter.submitList(filteredActorList)
                } else {
                    val stuffFullList = stuffList.filterNot { it.professionKey == "ACTOR" }
                    actorFullListAdapter.submitList(stuffFullList)
                    binding.actorFullTitleTextView.text = getString(R.string.stuff)
                }
            }
        }

        binding.actorFullBackButton.setOnClickListener {
            navController.popBackStack()
        }
    }

}