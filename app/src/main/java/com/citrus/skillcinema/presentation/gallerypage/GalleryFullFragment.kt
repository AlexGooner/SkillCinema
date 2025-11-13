package com.citrus.skillcinema.presentation.gallerypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.citrus.skillcinema.R
import com.citrus.skillcinema.data.models.GalleryItem
import com.citrus.skillcinema.databinding.FragmentGalleryFullBinding
import com.citrus.skillcinema.presentation.adapters.gallery.GalleryFullListAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFullFragment : Fragment() {

    private var _binding: FragmentGalleryFullBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryFullViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var galleryFullAdapter: GalleryFullListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryFullBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val args: GalleryFullFragmentArgs by navArgs()
        val movieId = args.movieId

        navController = findNavController()
        binding.progressBarGalleryFull.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBarGalleryFull.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.galleryFullRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }

        galleryFullAdapter = GalleryFullListAdapter(navController)
        binding.galleryFullRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.galleryFullRecyclerView.adapter = galleryFullAdapter

        viewModel.loadFanArts(movieId)
        viewModel.loadConcepts(movieId)
        viewModel.loadCovers(movieId)
        viewModel.loadPromos(movieId)
        viewModel.loadStills(movieId)
        viewModel.loadPosters(movieId)
        viewModel.loadShootings(movieId)
        viewModel.loadWallpapers(movieId)
        viewModel.loadScreenshots(movieId)

        setChipTitle(
            viewModel.asStillList,
            binding.stillChipGallery,
            "Кадры"
        )

        setChipTitle(
            viewModel.asPosterList,
            binding.postersChipGallery,
            "Постеры"
        )

        setChipTitle(
            viewModel.asFanArtList,
            binding.fanArtsChipGallery,
            "Фан-арты"
        )

        setChipTitle(
            viewModel.asPromoList,
            binding.promoChipGallery,
            "Промо"
        )
        setChipTitle(
            viewModel.asShootingList,
            binding.shootingChipGallery,
            "Со съемок"
        )
        setChipTitle(
            viewModel.asConceptList,
            binding.conceptsChipGallery,
            "Концепт-арты"
        )
        setChipTitle(
            viewModel.asWallpaperList,
            binding.wallpapersChipGallery,
            "Обои"
        )

        setChipTitle(
            viewModel.asCoverList,
            binding.coversChipGallery,
            "Обложки"
        )
        setChipTitle(
            viewModel.asScreenshotList,
            binding.screenshotsChipGallery,
            "Скриншоты"
        )

        binding.stillChipGallery.isChecked = true
        viewModel.asStillList.onEach {
            galleryFullAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)


        binding.galleryChipGroup.setOnCheckedStateChangeListener { group, _ ->
            when (group.checkedChipId) {
                R.id.still_chip_gallery -> {
                    viewModel.asStillList.onEach {

                        Log.d("STILL FRAGMENT", "STILL LIST $it")
                        galleryFullAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.shooting_chip_gallery -> {
                    viewModel.asShootingList.onEach {
                        galleryFullAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.posters_chip_gallery -> {
                    viewModel.asPosterList.onEach {
                        galleryFullAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.fan_arts_chip_gallery -> {
                    viewModel.asFanArtList.onEach {
                        galleryFullAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.promo_chip_gallery -> {
                    viewModel.asPromoList.onEach {
                        galleryFullAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.concepts_chip_gallery -> {
                    viewModel.asConceptList.onEach {
                        galleryFullAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.wallpapers_chip_gallery -> {
                    viewModel.asWallpaperList.onEach {
                        galleryFullAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.covers_chip_gallery -> {
                    viewModel.asCoverList.onEach {
                        galleryFullAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.screenshots_chip_gallery -> {
                    viewModel.asScreenshotList.onEach {
                        galleryFullAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

            }

        }

        binding.galleryFullBackButton.setOnClickListener {
            navController.popBackStack()
        }


    }

    private fun setChipTitle(
        stateFlowGalleryItems: StateFlow<List<GalleryItem>>,
        chip: Chip,
        title: String
    ) {
        stateFlowGalleryItems.onEach { imageList ->
            if (imageList.isEmpty()) chip.visibility = View.GONE
            else {
                with(chip) {
                    visibility = View.VISIBLE
                    text = title
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}