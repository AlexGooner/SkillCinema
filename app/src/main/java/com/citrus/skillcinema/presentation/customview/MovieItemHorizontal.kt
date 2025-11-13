package com.citrus.skillcinema.presentation.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.citrus.skillcinema.databinding.FilteredMovieItemBinding

class MovieItemHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = FilteredMovieItemBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setTitle(title: String?) {
        binding.title.text = title
    }

    @SuppressLint("SetTextI18n")
    fun setSubtitle(year: String?, genres: String?) {
        binding.genres.text = "$year $genres"
    }

    @SuppressLint("SetTextI18n")
    fun setRating(rating: Double?) {
        if (rating == null) binding.rating.visibility = GONE
        else {
            binding.rating.visibility = VISIBLE
            binding.rating.text = rating.toString()
        }
    }

    fun setPicture(url: String?) {
        Glide.with(binding.poster.context)
            .load(url)
            .into(binding.poster)
    }

}