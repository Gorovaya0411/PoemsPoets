package com.example.poemspoets.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.activities.R
import kotlinx.android.synthetic.main.item_view_poems_poets.view.*


class AdapterMyJob(private var callback: (PoemAnswer, Int) -> Unit) :
    RecyclerView.Adapter<AdapterMyJob.MyViewHolder>() {
    var dataTest = mutableListOf<PoemAnswer?>()
    fun setData(data: MutableList<PoemAnswer?>) {
        this.dataTest = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view_detailed_my_job,
                parent,
                false
            ), callback
        )
    }

    override fun getItemCount(): Int = dataTest.count()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        dataTest[position]?.let { holder.bind(it) }
    }


    class MyViewHolder(itemView: View, private var callback: (PoemAnswer, Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private var title: TextView = itemView.textViewTitle
        private var name: TextView = itemView.textViewNamePoetOrUser
        private var genre: TextView = itemView.textViewGenre
        var nameModified = ""
        private var like: TextView = itemView.textViewNumberLikes

        fun bind(model: PoemAnswer) {
            nameModified = model.namePoet.replace("|", ".", true)
            if (model.namePoet == "") {
                name.text = model.username

            } else {
                name.text = nameModified
            }
            title.text = model.titlePoem

            genre.text = model.genre
            like.text = model.like.toString()

            itemView.setOnClickListener {
                callback.invoke(model, 1)
            }
            itemView.setOnLongClickListener {
                callback.invoke(model, 2)
                return@setOnLongClickListener true
            }

        }
    }
}