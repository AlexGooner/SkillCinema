package com.citrus.skillcinema.presentation.adapters.actorfullist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.skillcinema.data.models.Stuff
import com.citrus.skillcinema.databinding.ActorItemBinding
import com.citrus.skillcinema.presentation.actorfulllist.ActorFullListFragmentDirections
import com.citrus.skillcinema.presentation.adapters.certainmovie.DiffUtilCallbackStuff
import com.citrus.skillcinema.presentation.adapters.certainmovie.StuffViewHolder

class ActorFullListAdapter(private val navController: NavController, private val context: Context) :
    ListAdapter<Stuff, RecyclerView.ViewHolder>(DiffUtilCallbackStuff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ActorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams = binding.root.layoutParams
        layoutParams.width = dpToPx(350f)
        layoutParams.height = dpToPx(90f)
        binding.root.layoutParams = layoutParams
        return StuffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StuffViewHolder -> {
                val item = getItem(position)
                with(holder.binding) {

                    val actorNameLayoutParams = actorNameTextView.layoutParams
                    actorNameLayoutParams.height = dpToPx(35f)
                    actorNameTextView.textSize = 18F
                    actorNameTextView.text = item?.nameRu ?: item?.nameEn

                    val actorRoleLayoutParams = actorRoleTextView.layoutParams
                    actorRoleLayoutParams.height = dpToPx(28f)
                    actorRoleTextView.textSize = 14F
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

                    val imageViewLayoutParams = actorImageView.layoutParams
                    imageViewLayoutParams.width = dpToPx(80f)
                    imageViewLayoutParams.height = dpToPx(100f)
                    actorImageView.layoutParams = imageViewLayoutParams
                    item?.let {
                        Glide.with(actorImageView).load(it.posterUrl).into(actorImageView)
                    }
                    holder.itemView.setOnClickListener {
                        val staffId = item?.staffId
                        if (staffId != null) {
                            val action =
                                ActorFullListFragmentDirections.actionNavigationActorFullToActorPage(
                                    staffId
                                )
                            navController.navigate(action)
                        }
                    }

                }
            }
        }
    }

    private fun dpToPx(dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }


}