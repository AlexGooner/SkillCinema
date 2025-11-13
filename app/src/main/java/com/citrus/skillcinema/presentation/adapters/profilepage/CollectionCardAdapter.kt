package com.citrus.skillcinema.presentation.adapters.profilepage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.citrus.skillcinema.R
import com.citrus.skillcinema.data.database.DefaultCollectionsName
import com.citrus.skillcinema.data.database.FilmsCollection
import com.citrus.skillcinema.data.database.SavedCollection
import com.citrus.skillcinema.databinding.CollectionCardBinding

class CollectionCardAdapter :
    ListAdapter<FilmsCollection, CollectionCardHolder>(CollectionCardDiffUtilCallBack()) {

    var onCloseClick: ((String) -> Unit)? = null
    var onItemClick: ((String) -> Unit)? = null
    private var filmsCount: Int = 0
    private val collectionsName = DefaultCollectionsName()


    override fun submitList(list: List<FilmsCollection>?) {
        val updatedList = ensureDefaultCollections(list ?: emptyList())
        super.submitList(updatedList)
    }

    private fun ensureDefaultCollections(list: List<FilmsCollection>): List<FilmsCollection> {
        val defaultCollections = listOf(
            FilmsCollection(SavedCollection(collectionsName.liked), emptyList()),
            FilmsCollection(SavedCollection(collectionsName.wantWatch), emptyList())
        )

        val existingCollections = list.toMutableList()

        // Добавляем обязательные коллекции, если их нет в списке
        defaultCollections.forEach { defaultCollection ->
            if (existingCollections.none { it.savedCollection.collectionName == defaultCollection.savedCollection.collectionName }) {
                existingCollections.add(defaultCollection)
            }
        }

        return existingCollections
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CollectionCardHolder, position: Int) {
        if (position < itemCount) {
            val item = getItem(position)

            if (item.savedCollection.collectionName == collectionsName.liked) {
                holder.binding.close.visibility = View.GONE
                holder.binding.filmsCount.text = filmsCount.toString()
                holder.binding.icon.setImageResource(R.drawable.ic_heart)
            }
            if (item.savedCollection.collectionName == collectionsName.wantWatch) {
                holder.binding.close.visibility = View.GONE
                holder.binding.filmsCount.text = filmsCount.toString()
                holder.binding.icon.setImageResource(R.drawable.ic_bookmark)
            }
            with(holder.binding) {
                collectionName.text = item.savedCollection.collectionName
                filmsCount.text = item.collectionFilms?.size.toString()
                touchZone.setOnClickListener {
                    onItemClick?.invoke(item.savedCollection.collectionName)
                }
                close.setOnClickListener {
                    onCloseClick?.invoke(item.savedCollection.collectionName)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionCardHolder {
        val binding = CollectionCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionCardHolder(binding)
    }

    fun updateFilmsCount(count: Int) {
        filmsCount = count
    }


}

class CollectionCardDiffUtilCallBack : DiffUtil.ItemCallback<FilmsCollection>() {
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

class CollectionCardHolder(val binding: CollectionCardBinding) :
    RecyclerView.ViewHolder(binding.root)
