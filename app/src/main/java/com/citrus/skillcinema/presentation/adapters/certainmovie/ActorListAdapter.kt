package com.citrus.skillcinema.presentation.adapters.certainmovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.databinding.ActorItemBinding
import com.citrus.skillcinema.data.models.Stuff
import com.citrus.skillcinema.presentation.certainmovie.CertainMovieFragmentDirections


class ActorListAdapter(
    private val navController: NavController
) : ListAdapter<Stuff, RecyclerView.ViewHolder>(DiffUtilCallbackStuff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StuffViewHolder(
            ActorItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StuffViewHolder -> {
                val item = getItem(position)
                with(holder.binding) {
                    actorNameTextView.text = item?.nameRu ?: item?.nameEn ?: ""
                    actorRoleTextView.text = item?.description ?: when (item?.professionKey) {
                        "OPERATOR" -> "Оператор"
                        "DIRECTOR" -> "Режиссер"
                        "PRODUCER" -> "Продюсер"
                        "WRITER" -> "Сценарист"
                        "EDITOR" -> "Редактор"
                        "COMPOSER" -> "Композитор"
                        "VOICE_DIRECTOR" -> "Звукорежиссер"
                        "TRANSLATOR" -> "Переводчик"
                        "DESIGN" -> "Дизайнер"
                        else -> ""
                    }
                    item?.let {
                        Glide.with(actorImageView).load(it.posterUrl).into(actorImageView)
                    }
                    holder.itemView.setOnClickListener {
                        val staffId = item?.staffId
                        if (staffId != null) {
                            val action = CertainMovieFragmentDirections.actionNavigationCertainMovieToNavigationActorPage4(staffId)
                            navController.navigate(action)
                        }
                    }
                }
            }
        }
    }


}

class StuffViewHolder(val binding: ActorItemBinding) : RecyclerView.ViewHolder(binding.root)

class DiffUtilCallbackStuff : DiffUtil.ItemCallback<Stuff>() {
    override fun areItemsTheSame(oldItem: Stuff, newItem: Stuff): Boolean =
        oldItem.staffId == newItem.staffId

    override fun areContentsTheSame(oldItem: Stuff, newItem: Stuff): Boolean =
        oldItem == newItem
}

