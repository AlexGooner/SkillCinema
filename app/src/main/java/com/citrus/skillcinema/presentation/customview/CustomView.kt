package com.citrus.skillcinema.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.citrus.skillcinema.R

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val leftTextView: TextView
    val rightTextView: TextView
    private val recyclerView: RecyclerView

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_view, this)
        leftTextView = findViewById(R.id.leftTextView)
        rightTextView = findViewById(R.id.rightTextView)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setLeftText(text: String) {
        leftTextView.text = text
    }

    fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
    }


    fun setOnRightTextClickListener(listener: OnClickListener) {
        rightTextView.setOnClickListener(listener)
    }
}