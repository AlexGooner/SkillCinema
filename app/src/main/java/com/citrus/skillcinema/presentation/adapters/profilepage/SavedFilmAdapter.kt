package com.citrus.skillcinema.presentation.adapters.profilepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.R
import com.citrus.skillcinema.data.database.NewSavedFilm
import com.citrus.skillcinema.databinding.MovieItemBinding
import com.citrus.skillcinema.databinding.ViewAllFilmsButtonBinding
import com.citrus.skillcinema.presentation.adapters.homepage.MovieViewHolder

open class SavedFilmAdapter :
    ListAdapter<NewSavedFilm, RecyclerView.ViewHolder>(SavedFilmDiffUtilCallBack()) {

    var onItemClick: ((Int) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> R.drawable.arrow_right
            else -> R.layout.movie_item
        }
    }

    private var watchedFilmIds: List<Int> = emptyList()



    override fun submitList(list: List<NewSavedFilm>?) {
        val mutableList = list?.toMutableList()
        mutableList?.add(NewSavedFilm(null, "", 1, "", "", null, ""))
        super.submitList(mutableList)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val titleText = item.name
        val genresText = item.genres
        val ratingText = item.rating
        when (getItemViewType(position)) {
            R.layout.movie_item -> {
                with((holder as MovieViewHolder).binding) {
                    title.text = titleText
                    genres.text = genresText
                    if (item?.rating != null) {
                        rating.text = ratingText.toString()
                    } else {
                        rating.visibility = View.GONE
                    }
                    if (watchedFilmIds.contains(item.filmId)){
                        title.visibility = View.GONE
                        gradient.visibility = View.VISIBLE
                    } else {
                        gradient.visibility = View.GONE
                    }
                    item.let {
                        Glide.with(poster.context)
                            .load(it?.url)
                            .into(poster)
                    }
                    root.setOnClickListener {
                        item?.let {
                            onItemClick?.invoke(item.filmId)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.movie_item -> {
                val binding =
                    MovieItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                MovieViewHolder(binding)
            }

            R.layout.view_all_films_button -> {
                val binding = ViewAllFilmsButtonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ButtonViewHolder(binding)
            }

            else -> {
                throw IllegalArgumentException("unknown view type $viewType")
            }
        }
    }
}

class SavedFilmDiffUtilCallBack : DiffUtil.ItemCallback<NewSavedFilm>() {
    override fun areItemsTheSame(
        oldItem: NewSavedFilm,
        newItem: NewSavedFilm
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: NewSavedFilm,
        newItem: NewSavedFilm
    ): Boolean =
        oldItem.name == newItem.name
}

class ButtonViewHolder(val binding: ViewAllFilmsButtonBinding) :
    RecyclerView.ViewHolder(binding.root)
