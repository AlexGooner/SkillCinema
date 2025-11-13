package com.citrus.skillcinema.presentation.adapters.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.databinding.FilteredMovieItemBinding
import com.citrus.skillcinema.data.models.FilteredFilmsList
import com.citrus.skillcinema.presentation.searchpage.SearchFragmentDirections


class SearchAdapter(private val navController: NavController) :
    ListAdapter<FilteredFilmsList, FilteredMovieViewHolder>(
        DiffUtilCallbackFilteredMovie()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilteredMovieViewHolder {
        val binding = FilteredMovieItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FilteredMovieViewHolder(binding)
    }
    private var watchedFilmIds: List<Int> = emptyList()


    @SuppressLint("NotifyDataSetChanged")
    fun setWatchedFilmIds(ids: List<Int>) {
        watchedFilmIds = ids
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilteredMovieViewHolder, position: Int) {

        val item = getItem(position)
        val titleText = item.nameRu ?: item.nameEn
        val descriptionText = item.year.toString() + ", " +
                item.genres.joinToString(", ") { it.genre ?: "" }

        with(holder.binding) {
            title.text = titleText
            genres.text = descriptionText
            if (item.ratingKinopoisk != null){
                rating.text = item?.ratingKinopoisk.toString()
                rating.visibility = View.VISIBLE
            } else {
                rating.visibility = View.GONE
            }
            if (watchedFilmIds.contains(item.kinopoiskId)){
                gradient.visibility = View.VISIBLE
            } else {
                gradient.visibility = View.GONE
            }
            item?.let {
                Glide.with(poster.context)
                    .load(it.posterUrlPreview)
                    .into(poster)
            }
            root.setOnClickListener {
                val action = SearchFragmentDirections.navigationSearchToCertain(item.kinopoiskId)
                navController.navigate(action)
            }
        }
    }

}


class FilteredMovieViewHolder(val binding: FilteredMovieItemBinding) :
    RecyclerView.ViewHolder(binding.root)

class DiffUtilCallbackFilteredMovie : DiffUtil.ItemCallback<FilteredFilmsList>() {
    override fun areItemsTheSame(oldItem: FilteredFilmsList, newItem: FilteredFilmsList): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(
        oldItem: FilteredFilmsList,
        newItem: FilteredFilmsList
    ): Boolean =
        oldItem == newItem
}
