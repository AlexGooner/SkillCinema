package com.citrus.skillcinema.presentation.gallerypage.fullscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.citrus.skillcinema.databinding.FragmentImageViewerBinding
import com.citrus.skillcinema.presentation.adapters.gallery.fullscreen.ImageSliderAdapter

class ImageViewerFragment : Fragment() {

    private var _binding: FragmentImageViewerBinding? = null
    private val binding get() = _binding!!

    private var currentPosition: Int = 0
    private lateinit var imageUrls: List<String>




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageViewerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController : NavController = findNavController()

        val args: ImageViewerFragmentArgs by navArgs()
        imageUrls = args.imageUrls.toList()
        currentPosition = args.position

        val adapter = ImageSliderAdapter(imageUrls)
        binding.viewPager.adapter = adapter


        binding.viewPager.setCurrentItem(currentPosition, false)

        binding.closeButton.setOnClickListener {
            navController.popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}