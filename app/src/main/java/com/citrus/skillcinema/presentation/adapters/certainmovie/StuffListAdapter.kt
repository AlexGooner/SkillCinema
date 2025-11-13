package com.citrus.skillcinema.presentation.adapters.certainmovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.data.models.Stuff
import com.citrus.skillcinema.databinding.ActorItemBinding
import com.citrus.skillcinema.presentation.certainmovie.CertainMovieFragmentDirections

class StuffListAdapter(
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
                    val professionText = item?.professionKeys?.joinToString(", ") {key ->
                        when(key) {
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
                    } ?: " "
                    actorRoleTextView.text = professionText

                    item?.let {
                        Glide.with(actorImageView).load(it.posterUrl).into(actorImageView)
                    }

                    holder.itemView.setOnClickListener {
                        val action =
                            CertainMovieFragmentDirections.actionNavigationCertainMovieToNavigationActorPage4(
                                item.staffId
                            )
                        navController.navigate(action)
                    }
                }
            }
        }
    }

    fun mergeStuffList(stuffList : List<Stuff>) : List<Stuff> {
        val mergedMap = mutableMapOf<String, Stuff>()

        for (stuff in stuffList) {
            val name = stuff.nameRu ?: stuff.nameEn ?: continue
            if (mergedMap.containsKey(name)) {
                val existingStuff = mergedMap[name]!!
                val updatedProfessionKeys = existingStuff.professionKeys + stuff.professionKey
                mergedMap[name] = existingStuff.copy(professionKeys = updatedProfessionKeys)
            } else {
                mergedMap[name] = stuff.copy(professionKeys = listOf(stuff.professionKey))
            }
        }
        return mergedMap.values.toList()
    }
}