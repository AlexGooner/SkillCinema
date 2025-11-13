package com.citrus.skillcinema.presentation.adapters.filmography

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.citrus.skillcinema.databinding.FilmographyItemBinding
import com.citrus.skillcinema.data.models.ActorFilm
import com.citrus.skillcinema.presentation.filmography.FilmographyFragmentDirections

class FilmographyAdapter(
    private val navController: NavController
) : ListAdapter<ActorFilm, FilmographyViewHolder>(DiffUtilCallbackFilmography()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmographyViewHolder {
        val binding = FilmographyItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FilmographyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilmographyViewHolder, position: Int) {
        val item = getItem(position)
        val titleText = if (item?.nameRu == null) item.nameEn else item.nameRu
        val description = if (item?.description == "") {
            when (item.professionKey) {
                "WRITER" -> "Сценарист"
                "DIRECTOR" -> "Режиссер"
                "PRODUCER" -> "Продюсер"
                "ACTOR" -> "Играет персонажа"
                "HIMSELF" -> "В роли себя"
                else -> "Другое"
            }
        } else item.description
        with(holder.binding) {
            title.text = titleText
            subtitle.text = description
        }

        holder.itemView.setOnClickListener {
            val action = FilmographyFragmentDirections.actionNavigationFilmographyToNavigationCertainMovie(item.filmId)
            navController.navigate(action)
        }
    }
}

class FilmographyViewHolder(val binding: FilmographyItemBinding) :
    RecyclerView.ViewHolder(binding.root)

class DiffUtilCallbackFilmography : DiffUtil.ItemCallback<ActorFilm>() {
    override fun areItemsTheSame(oldItem: ActorFilm, newItem: ActorFilm): Boolean =
        oldItem.filmId == newItem.filmId

    override fun areContentsTheSame(oldItem: ActorFilm, newItem: ActorFilm): Boolean =
        oldItem == newItem
}