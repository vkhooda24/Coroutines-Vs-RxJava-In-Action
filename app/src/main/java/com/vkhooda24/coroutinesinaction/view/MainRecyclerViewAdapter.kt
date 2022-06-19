package com.vkhooda24.coroutinesinaction.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vkhooda24.coroutinesinaction.R
import com.vkhooda24.coroutinesinaction.service.Country

class MainRecyclerViewAdapter(
    private val countriesList: List<Country>,
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.countries_list_row, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val index = position.inc()
        val name = "$index: ${countriesList[position].name.common}"
        viewHolder.countryNameTextView.text = name
    }

    override fun getItemCount(): Int = countriesList.size
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val countryNameTextView: TextView = view.findViewById(R.id.countryNameTextView)
}