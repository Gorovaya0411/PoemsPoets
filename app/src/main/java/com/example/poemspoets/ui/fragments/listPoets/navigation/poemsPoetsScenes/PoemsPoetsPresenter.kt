package com.example.poemspoets.ui.fragments.listPoets.navigation.poemsPoetsScenes

import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.AdapterForPoet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import moxy.MvpPresenter

class PoemsPoetsPresenter : MvpPresenter<PoemsPoetsView>() {
    private val myAdapter =
        AdapterForPoet { openingNewActivity(it) }
    private var listPoemPoet: MutableList<PoemAnswer?> = mutableListOf()
    private var listPoemPoetRand: List<PoemAnswer?> = mutableListOf()

    fun getData() {
        viewState.workWithAdapter(myAdapter)
        viewState.workWithSearchWidget(myAdapter)
        val refUser = FirebaseDatabase.getInstance().reference.child("Poem")
        refUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val children = p0.children
                children.forEach {
                    val poem: PoemAnswer? = it.getValue(PoemAnswer::class.java)
                    if (poem!!.namePoet != "") {
                        listPoemPoet.add(poem)
                        listPoemPoetRand = listPoemPoet.shuffled()
                    }
                }
                populateData(listPoemPoetRand as MutableList<PoemAnswer?>)
            }
        })
    }

    private fun populateData(poems: MutableList<PoemAnswer?>) {
        myAdapter.setData(poems)
    }

    private fun openingNewActivity(model: PoemAnswer) {
        viewState.openingNewActivity(model)
    }

}