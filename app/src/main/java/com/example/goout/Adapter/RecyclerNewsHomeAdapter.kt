package com.example.goout.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.goout.Model.Evaluate
import com.example.goout.R
import kotlinx.android.synthetic.main.news_adapter.view.*

class RecyclerSuggestionsHomeAdapter(private val news: List<Evaluate>,
                                     private val context: Context) :
    RecyclerView.Adapter<RecyclerSuggestionsHomeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.titleNews
        val description: TextView = itemView.descriptionNews
        val category: TextView = itemView.categoryNews
        val icons: TextView = itemView.icons
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val layout = LayoutInflater.from(context).inflate(R.layout.news_adapter, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val new = news[position]
        holder.title.text = new.establishment.name
        holder.category.text = new.establishment.category
        holder.description.text = new.description
        holder.icons.text = new.establishment.acessibility.toString()
    }

}
