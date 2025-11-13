package com.citrus.skillcinema.presentation.adapters.certainmovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.databinding.GalleryItemBinding
import com.citrus.skillcinema.data.models.GalleryItem
import com.citrus.skillcinema.presentation.certainmovie.CertainMovieFragmentDirections

class GalleryListAdapter(
    private val navController: NavController
) : ListAdapter<GalleryItem, RecyclerView.ViewHolder>(DiffUtilCallbackGallery()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = GalleryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is GalleryViewHolder) {
            with(holder.binding) {
                item?.let {
                    Glide.with(galleryImageView.context)
                        .load(it.imageUrl)
                        .into(galleryImageView)
                }
                root.setOnClickListener {
                    val action = CertainMovieFragmentDirections.actionCertainToImageViewer(getImageUrls(), position)
                    navController.navigate(action)
                }
            }
        }
    }
    private fun getImageUrls(): Array<String> {
        return currentList.map { it.imageUrl }.toTypedArray()
    }
}

class GalleryViewHolder(val binding: GalleryItemBinding) : RecyclerView.ViewHolder(binding.root)


class DiffUtilCallbackGallery : DiffUtil.ItemCallback<GalleryItem>() {
    override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
        oldItem.imageUrl == newItem.imageUrl

    override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
        oldItem == newItem

}