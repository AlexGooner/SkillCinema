package com.citrus.skillcinema.presentation.adapters.search.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.citrus.skillcinema.R

class YearFromToAdapter (
    private val years: List<Int>,
    private val onYearClick: (Int) -> Unit
) : RecyclerView.Adapter<YearFromToAdapter.YearViewHolder>() {

    class YearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val yearText: TextView = itemView.findViewById(R.id.year_item)
    }

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.year_item, parent, false)
        return YearViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged", "ResourceAsColor")
    override fun onBindViewHolder(holder: YearViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val year = years[position]
        holder.yearText.text = year.toString()

        if (position == selectedPosition) {
            holder.yearText.setBackgroundResource(R.drawable.year_background_selected)
        } else {
            holder.yearText.setBackgroundResource(R.drawable.year_background)
        }


        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            onYearClick(year)
        }
    }

    override fun getItemCount(): Int {
        return years.size
    }
    fun getSelectedYear(): Int? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            years[selectedPosition]
        } else {
            null
        }
    }
}