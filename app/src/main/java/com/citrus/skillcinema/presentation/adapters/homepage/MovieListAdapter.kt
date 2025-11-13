package com.citrus.skillcinema.presentation.adapters.homepage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.R
import com.citrus.skillcinema.databinding.MovieItemBinding
import com.citrus.skillcinema.data.models.Movie
import com.citrus.skillcinema.presentation.homepage.MovieFragmentDirections

class MovieListAdapter(
    private val navController: NavController,
    private val movieType: String,
) : ListAdapter<Movie, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    private val MOVIE_VIEW_TYPE = 0
    private val SHOW_ALL_VIEW_TYPE = 1

    private var watchedFilmIds: List<Int> = emptyList()


    @SuppressLint("NotifyDataSetChanged")
    fun setWatchedFilmIds(ids: List<Int>) {
        watchedFilmIds = ids
        notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) SHOW_ALL_VIEW_TYPE else MOVIE_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SHOW_ALL_VIEW_TYPE) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_show_all, parent, false)
            ShowAllViewHolder(view)
        } else {
            MovieViewHolder(
                MovieItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            val item = getItem(position)
            with(holder.binding) {
                title.text = item?.nameRu ?: item?.nameOriginal ?: ""
                genres.text = item?.genres?.joinToString(", ") { it.genre.toString() }
                rating.text = item?.ratingKinopoisk.toString()
                item?.ratingKinopoisk?.let { ratingValue ->
                    rating.text = ratingValue.toString()
                    rating.visibility = View.VISIBLE
                } ?: run {
                    rating.visibility = View.GONE
                }


                item?.let {
                    Glide.with(poster.context).load(it.posterUrlPreview).into(poster)
                }
                if (watchedFilmIds.contains(item.kinopoiskId)){
                    gradient.visibility = View.VISIBLE
                } else {
                    gradient.visibility = View.GONE
                }

                holder.itemView.setOnClickListener {
                    val action =
                        MovieFragmentDirections.actionNavigationHomeToCertainMovie(item.kinopoiskId)
                    navController.navigate(action)
                }
            }
        } else if (holder is ShowAllViewHolder) {
            holder.showAllButton.setOnClickListener {
                click()
            }
        }


    }

    fun click() {
        when (movieType) {
            "TOP_POPULAR_MOVIES" -> {
                val action =
                    MovieFragmentDirections.actionNavigationHomeToNavigationAllMovies("TOP_POPULAR_MOVIES")
                navController.navigate(action)
            }

            "PREMIERS" -> {
                val bundle = Bundle().apply {
                    putString("movieType", "PREMIERS")
                    putInt("year", 2025)
                    putString("month", "MARCH")
                }
                navController.navigate(R.id.action_navigation_home_to_navigation_all_movies, bundle)
            }

            "TOP_250_TV_SHOWS" -> {
                val action =
                    MovieFragmentDirections.actionNavigationHomeToNavigationAllMovies("TOP_250_TV_SHOWS")
                navController.navigate(action)
            }

            "TOP_250_MOVIES" -> {
                val action =
                    MovieFragmentDirections.actionNavigationHomeToNavigationAllMovies("TOP_250_MOVIES")
                navController.navigate(action)
            }
        }
    }

    inner class ShowAllViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showAllButton: Button = itemView.findViewById(R.id.showAllButton)
    }

}

class MovieViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

class DiffUtilCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem
}