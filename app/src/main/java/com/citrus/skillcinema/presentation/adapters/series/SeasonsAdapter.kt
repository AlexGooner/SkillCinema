package com.citrus.skillcinema.presentation.adapters.series

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.citrus.skillcinema.databinding.SeasonsItemBinding
import com.citrus.skillcinema.data.models.Episode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SeasonsAdapter(
    private val navController: NavController
) : ListAdapter<Episode, SeasonsViewHolder>(DiffUtilCallbackSeasons()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonsViewHolder {
        val binding = SeasonsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SeasonsViewHolder(binding)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SeasonsViewHolder, position: Int) {
        val item = getItem(position)
        val episodeName = if (item?.nameRu == null) item.nameEn ?: "" else item.nameRu
        val titleText = "${item.episodeNumber} серия. $episodeName"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val data =
            if (item.releaseDate != null) LocalDate.parse(item.releaseDate, formatter) else null

        val subtitleText =
            if (data != null) data.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
            else " "
        with(holder.binding){
            seasonsTitle.text = titleText
            seasonsDate.text = subtitleText
        }
    }


}

class SeasonsViewHolder(val binding: SeasonsItemBinding) :
    RecyclerView.ViewHolder(binding.root)

class DiffUtilCallbackSeasons : DiffUtil.ItemCallback<Episode>() {
    override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean =
        oldItem.nameRu == newItem.nameRu

    override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean =
        oldItem == newItem
}

