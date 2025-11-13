package com.citrus.skillcinema.presentation.adapters.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.citrus.skillcinema.databinding.GalleryFullItemBinding
import com.citrus.skillcinema.data.models.GalleryItem
import com.citrus.skillcinema.presentation.gallerypage.GalleryFullFragmentDirections

class GalleryFullListAdapter(
    private val navController: NavController
) : ListAdapter<GalleryItem, GalleryFullViewHolder>(DiffUtilCallbackGalleryFull()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryFullViewHolder {
        val binding = GalleryFullItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GalleryFullViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryFullViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            item?.let {
                Glide.with(imageViewLarge)
                    .load(it.imageUrl)
                    .into(imageViewLarge)
            }
            root.setOnClickListener {

                val action = GalleryFullFragmentDirections.actionNavigationGalleryFullToImageViewer(
                    imageUrls = getImageUrls(),
                    position = position
                )
                navController.navigate(action)
            }
        }
    }

    private fun getImageUrls(): Array<String> {
        return currentList.map { it.imageUrl }.toTypedArray()
    }
}

class DiffUtilCallbackGalleryFull : DiffUtil.ItemCallback<GalleryItem>() {
    override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
        oldItem.imageUrl == newItem.imageUrl

    override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
        oldItem == newItem
}

class GalleryFullViewHolder(val binding: GalleryFullItemBinding) :
    RecyclerView.ViewHolder(binding.root)
