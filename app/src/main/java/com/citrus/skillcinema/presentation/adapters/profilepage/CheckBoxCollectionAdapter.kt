package com.citrus.skillcinema.presentation.adapters.profilepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.citrus.skillcinema.data.database.DefaultCollectionsName
import com.citrus.skillcinema.data.database.FilmsCollection
import com.citrus.skillcinema.databinding.CheckBoxCollectionBinding

class CheckBoxCollectionAdapter(val filmId: Int) :
    ListAdapter<FilmsCollection, CheckBoxCollectionHolder>(CheckBoxCollectionDiffUtilCallBack()) {

    var needAdd: ((String) -> Unit)? = null

    private val collectionsName = DefaultCollectionsName()

    override fun submitList(list: List<FilmsCollection>?) {
        val mutableList: MutableList<FilmsCollection>? = list?.toMutableList()
        mutableList?.removeIf {
            it.savedCollection.collectionName == collectionsName.interested ||
                    it.savedCollection.collectionName == collectionsName.watched
        }
        super.submitList(mutableList)
    }

    override fun onBindViewHolder(holder: CheckBoxCollectionHolder, position: Int) {
        val item = getItem(position)
        val title = item.savedCollection.collectionName
        var filmsCount = item.collectionFilms?.size
        val isContain =
            item.collectionFilms!!.any { it.filmId == filmId && it.collectionName == title }

        holder.binding.checkBox.isChecked = isContain
        holder.binding.title.text = title
        holder.binding.itemCount.text = filmsCount.toString()
        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                needAdd?.invoke(title)
                filmsCount = filmsCount!! + 1
                holder.binding.itemCount.text = filmsCount.toString()
            } else {
                needAdd?.invoke(title)
                filmsCount = filmsCount!! - 1
                holder.binding.itemCount.text = filmsCount.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckBoxCollectionHolder {
        val binding = CheckBoxCollectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CheckBoxCollectionHolder(binding)
    }

}

class CheckBoxCollectionDiffUtilCallBack : DiffUtil.ItemCallback<FilmsCollection>() {
    override fun areItemsTheSame(
        oldItem: FilmsCollection,
        newItem: FilmsCollection
    ): Boolean =
        oldItem.savedCollection.collectionName == newItem.savedCollection.collectionName

    override fun areContentsTheSame(
        oldItem: FilmsCollection,
        newItem: FilmsCollection
    ): Boolean =
        oldItem.savedCollection.collectionName == newItem.savedCollection.collectionName
}

class CheckBoxCollectionHolder(val binding: CheckBoxCollectionBinding) :
    RecyclerView.ViewHolder(binding.root)
