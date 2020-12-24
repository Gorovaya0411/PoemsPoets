package com.example.poemspoets.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.activities.R
import kotlinx.android.synthetic.main.item_view_detailed_poet_and_user.view.*

class AdapterForDetailedPoetAndUser(private var callback: (PoemAnswer) -> Unit) :
    RecyclerView.Adapter<AdapterForDetailedPoetAndUser.MyViewHolder>() {
    private var dataTest = mutableListOf<PoemAnswer?>()
    fun setData(data: MutableList<PoemAnswer?>) {
        this.dataTest = data
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view_detailed_poet_and_user,
                parent,
                false
            ), callback
        )
    }


    override fun getItemCount(): Int = dataTest.count()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        dataTest[position]?.let { holder.bind(it) }
    }

    class MyViewHolder(itemView: View, private var callback: (PoemAnswer) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private var name: TextView = itemView.textViewTitle
        private var genre: TextView = itemView.textViewGenre
        private var like: TextView = itemView.textViewNumberLikes


        fun bind(model: PoemAnswer) {
            name.text = model.titlePoem
            genre.text = model.genre
            like.text = model.like.toString()

            itemView.setOnClickListener {
                callback.invoke(model)
            }

        }
    }
}

