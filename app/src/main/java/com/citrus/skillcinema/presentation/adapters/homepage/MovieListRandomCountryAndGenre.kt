package com.citrus.skillcinema.presentation.adapters.homepage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.R
import com.citrus.skillcinema.databinding.MovieItemBinding
import com.citrus.skillcinema.data.models.Movie
import com.citrus.skillcinema.presentation.homepage.MovieFragmentDirections
import com.citrus.skillcinema.presentation.homepage.MovieViewModel
import com.citrus.skillcinema.presentation.homepage.OnShowAllClickListener

class MovieListRandomCountryAndGenreAdapter(private val navController: NavController, private val listener: OnShowAllClickListener,
                                            private val viewModel: MovieViewModel
) :
    ListAdapter<Movie, RecyclerView.ViewHolder>(DiffUtilCallback()) {



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
                title.text = item?.nameRu ?: ""
                genres.text = item?.genres?.joinToString(", ") { it.genre.toString() }
                rating.text = item?.ratingKinopoisk.toString()
                item?.ratingKinopoisk?.let { ratingValue ->
                    rating.text = ratingValue.toString()
                    rating.visibility = View.VISIBLE
                } ?: run {
                    rating.visibility = View.GONE
                }
                item?.let {
                    Glide
                        .with(poster.context)
                        .load(it.posterUrlPreview)
                        .into(poster)
                }
                if (watchedFilmIds.contains(item.kinopoiskId)){
                    gradient.visibility = View.VISIBLE
                    title.visibility = View.GONE
                } else {
                    gradient.visibility = View.GONE
                }
                holder.itemView.setOnClickListener{
                    val action = MovieFragmentDirections.actionNavigationHomeToCertainMovie(item.kinopoiskId)
                    navController.navigate(action)
                }
            }
        } else if (holder is ShowAllViewHolder) {
            holder.showAllButton.setOnClickListener {
                val action = MovieFragmentDirections.actionNavigationHomeToNavigationAllMovies("RANDOM",
                    viewModel.currentCountry.value, viewModel.currentGenre.value
                )
                navController.navigate(action)
            }
        }


    }
    fun clickRandomAll() {
        val action = MovieFragmentDirections.actionNavigationHomeToNavigationAllMovies("RANDOM",
            viewModel.currentCountry.value, viewModel.currentGenre.value
        )
        navController.navigate(action)
    }



    inner class ShowAllViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showAllButton: Button = itemView.findViewById(R.id.showAllButton)
    }
}