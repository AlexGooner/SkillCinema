package com.citrus.skillcinema.presentation.adapters.profilepage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.R
import com.citrus.skillcinema.data.models.Movie
import com.citrus.skillcinema.databinding.DeleteAllItemBinding
import com.citrus.skillcinema.databinding.MovieItemBinding
import com.citrus.skillcinema.presentation.adapters.homepage.MovieViewHolder
import com.citrus.skillcinema.presentation.profilepage.ProfileFragmentDirections

class ProfileWatchedAdapter(
    private val navController: NavController,
    private val onDeleteAllClicked: () -> Unit
) : ListAdapter<Movie, RecyclerView.ViewHolder>(DiffUtilCallbackWatched()) {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_DELETE_ALL = 1
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size) {
            VIEW_TYPE_DELETE_ALL
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = MovieItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MovieViewHolder(binding)
            }
            VIEW_TYPE_DELETE_ALL -> {
                val binding = DeleteAllItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DeleteViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                val item = getItem(position)
                with(holder.binding) {
                    title.text = item?.nameRu ?: item?.nameOriginal
                    genres.text = item?.genres?.joinToString(", ") { it.genre.toString() }
                    if (item?.ratingKinopoisk != null) {
                        rating.text = item.ratingKinopoisk.toString()
                    } else {
                        rating.visibility = View.GONE
                    }
                    item?.let {
                        Glide.with(poster.context).load(it.posterUrlPreview).into(
                            poster
                        )
                    }
                }

                holder.itemView.setOnClickListener {
                    item?.let { movie ->
                        val action =
                            ProfileFragmentDirections.actionNavigationProfileToCertain(movie.kinopoiskId)
                        navController.navigate(action)
                    }
                }
            }
            is DeleteViewHolder -> {
                holder.itemView.findViewById<ImageView>(R.id.delete_all_button).setOnClickListener {
                    onDeleteAllClicked()
                }
            }
        }
    }


    class DeleteViewHolder(val binding: DeleteAllItemBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffUtilCallbackWatched : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.kinopoiskId == newItem.kinopoiskId

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem
    }
}