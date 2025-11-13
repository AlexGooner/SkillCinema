package com.citrus.skillcinema.presentation.adapters.allmovies

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.data.models.Movie
import com.citrus.skillcinema.databinding.MovieItemBinding
import com.citrus.skillcinema.presentation.allmovies.AllMoviesFragmentDirections

class AllMoviesAdapter(private val navController: NavController) : RecyclerView.Adapter<AllMoviesAdapter.MovieViewHolder>() {


    var movieList: List<Movie> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setMovies(newMovies: List<Movie>) {
        movieList = newMovies
        notifyDataSetChanged()
    }
    private var watchedFilmIds: List<Int> = emptyList()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movieList.size


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)

        holder.itemView.setOnClickListener {
            val action = AllMoviesFragmentDirections.actionNavigationAllMoviesToCertain(movie.kinopoiskId)
            navController.navigate(action)
        }
    }

    inner class MovieViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) {
            binding.title.text = movie.nameRu
            binding.genres.text = movie.genres?.joinToString(", " ) {it.genre.toString()}
            binding.rating.text = movie.ratingKinopoisk.toString()
            movie.ratingKinopoisk?.let { ratingValue ->
                binding.rating.text = ratingValue.toString()
                binding.rating.visibility = View.VISIBLE
            } ?: run{
                binding.rating.visibility = View.GONE
            }

            if (watchedFilmIds.contains(movie.kinopoiskId)){
                binding.gradient.visibility = View.VISIBLE
            } else {
                binding.gradient.visibility = View.GONE
            }
            Glide.with(binding.poster.context)
                .load(movie.posterUrlPreview)
                .into(binding.poster)
        }
    }
}