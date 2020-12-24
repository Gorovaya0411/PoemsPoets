package com.example.poemspoets.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.activities.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_poems_poets.view.*
import java.util.*


class AdapterForPoet(private var callback: (PoemAnswer) -> Unit) :
    RecyclerView.Adapter<AdapterForPoet.MyViewHolder>() {

    var dataTest = mutableListOf<PoemAnswer?>()
    var initialData = mutableListOf<PoemAnswer?>()
    fun setData(data: MutableList<PoemAnswer?>) {
        dataTest = data
        initialData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view_poems_poets,
                parent,
                false
            ), callback
        )
    }

    override fun getItemCount(): Int = dataTest.count()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        dataTest[position]?.let { holder.bind(it) }

    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                dataTest = if (charSearch.isEmpty()) {
                    initialData
                } else {
                    val resultList: MutableList<PoemAnswer?> = mutableListOf()
                    for (row in initialData) {
                        if (row!!.titlePoem.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT)) || row.namePoet.toLowerCase(
                                Locale.ROOT
                            )
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = dataTest
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataTest = results?.values as MutableList<PoemAnswer?>
                notifyDataSetChanged()
            }

        }
    }

    class MyViewHolder(itemView: View, private var callback: (PoemAnswer) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private var avatar: ImageView = itemView.imageViewAvatarUsers
        private var title: TextView = itemView.textViewTitle
        private var name: TextView = itemView.textViewNamePoetOrUser
        private var genre: TextView = itemView.textViewGenre
        private var like: TextView = itemView.textViewNumberLikes
        var namePoetModified = ""

        fun bind(model: PoemAnswer) {
            namePoetModified = model.namePoet.replace("|", ".", true)
            title.text = model.titlePoem
            like.text = model.like.toString()
            name.text = namePoetModified
            genre.text = model.genre
            Picasso.get()
                .load(model.avatar)
                .into(avatar)


            itemView.setOnClickListener {
                callback.invoke(model)
            }
        }
    }
}