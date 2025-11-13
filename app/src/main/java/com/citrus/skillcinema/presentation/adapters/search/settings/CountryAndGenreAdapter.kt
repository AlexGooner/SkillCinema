package com.citrus.skillcinema.presentation.adapters.search.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.citrus.skillcinema.R
import com.citrus.skillcinema.data.models.MovieData

class CountryAndGenreAdapter(
    private val ids: List<Int>,
    private val type: String,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<CountryAndGenreAdapter.CountryAndGenreViewHolder>() {

    private var filteredItems: List<Int> = ids

    class CountryAndGenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.country_and_genre_text_view_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryAndGenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_and_genre_item, parent, false)
        return CountryAndGenreViewHolder(itemView = view)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    override fun onBindViewHolder(holder: CountryAndGenreViewHolder, position: Int) {
        val itemId = filteredItems[position]
        val itemName = if (type == "country") {
            MovieData.getCountryInNominative(itemId)
        } else {
            MovieData.getGenreName(itemId)
        }
        holder.text.text = itemName
        holder.itemView.setOnClickListener {
            onItemClick(itemName)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterCountries(query: String) {
        filteredItems = if (query.isEmpty()) {
            ids
        } else {
            ids.filter {
                MovieData.getCountryInNominative(it).contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun filterGenres(query: String) {
        filteredItems = if (query.isEmpty()) {
            ids
        } else {
            ids.filter {
                MovieData.getGenreName(it).contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}