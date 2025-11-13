package com.citrus.skillcinema.presentation.adapters.certainmovie

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.databinding.MovieItemBinding
import com.citrus.skillcinema.data.models.Movie
import com.citrus.skillcinema.presentation.certainmovie.CertainMovieFragmentDirections

class SimilarMovieListAdapter(
    private val navController: NavController
) : ListAdapter<Movie, RecyclerView.ViewHolder>(DiffUtilCallbackSimilar()) {

    var onItemClick: ((Movie) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = MovieItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CertainMovieViewHolder(binding)
    }
    private var watchedFilmIds: List<Int> = emptyList()


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CertainMovieViewHolder) {
            val item = getItem(position)
            with(holder.binding) {
                title.text = item?.nameRu ?: ""
                genres.text = item?.genres?.joinToString(", ") { it.genre.toString() }
                if (item.ratingKinopoisk != null){
                    rating.text = item?.ratingKinopoisk.toString()
                    rating.visibility = View.VISIBLE
                } else {
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
                root.setOnClickListener {
                    val action = CertainMovieFragmentDirections.actionNavigationCertainMovieToAnotherCertainMovie(
                        item.filmId
                    )
                    navController.navigate(action)
                }
            }
        }
    }
}

class CertainMovieViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

class DiffUtilCallbackSimilar : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem


}