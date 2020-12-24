package com.example.poemspoets.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.activities.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_my_poem.view.*

class AdapterForMyPoem(private var callback: (PoemAnswer, Int) -> Unit) :
    RecyclerView.Adapter<AdapterForMyPoem.MyViewHolder>() {
    var dataTest = mutableListOf<PoemAnswer?>()
    fun setData(data: MutableList<PoemAnswer?>) {
        this.dataTest = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view_my_poem,
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
        private var avatar: ImageView = itemView.imageViewAvatarUsers
        private var title: TextView = itemView.textViewTitle
        private var name: TextView = itemView.textViewNamePoetOrUser
        private var genre: TextView = itemView.textViewGenre
        private var like: TextView = itemView.textViewNumberLikes
        private var nameModified = ""

        fun bind(model: PoemAnswer) {
            if (model.namePoet == "") {
                nameModified = model.username.replace("|", ".", true)
                name.text = nameModified
            } else {
                nameModified = model.namePoet.replace("|", ".", true)
                name.text = nameModified
            }
            title.text = model.titlePoem
            genre.text = model.genre
            like.text = model.like.toString()
            Picasso.get()
                .load(model.avatar)
                .into(avatar)



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