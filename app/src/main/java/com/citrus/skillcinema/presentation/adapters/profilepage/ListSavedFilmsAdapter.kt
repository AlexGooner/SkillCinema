package com.citrus.skillcinema.presentation.adapters.profilepage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.citrus.skillcinema.R
import com.citrus.skillcinema.presentation.adapters.homepage.MovieViewHolder

class ListSavedFilmsAdapter : SavedFilmAdapter() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == itemCount - 1) (holder as MovieViewHolder).binding.root.visibility = View.GONE
        super.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.movie_item
    }

}