package com.example.poemspoets.ui.activities.detailedPoemGeneralScenes

import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import moxy.MvpPresenter

class DetailedPoemGeneralPresenter : MvpPresenter<DetailedPoemGeneralView>() {
    var arrayAddPoem = emptyArray<String>()
    private var firebaseUser: FirebaseUser? = null
    private lateinit var idUser: String
    var arrayLike = emptyArray<String>()

    fun savingValueCheckBoxAddPoem(model: String) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        val refReceivingPoem =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                .child("MyAdded")
        refReceivingPoem.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val children = p0.children
                    children.forEach {
                        val poem: PoemAnswer? = it.getValue(PoemAnswer::class.java)
                        arrayAddPoem += poem!!.id

                    }
                    arrayAddPoem.forEach {
                        if (model == it) {
                            viewState.workWithCheckbox()
                        }
                    }
                }
            }
        })
    }

    fun addEmail() {
        val refReceivingPoem =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        refReceivingPoem.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    val email: User? = p0.getValue(User::class.java)
                    idUser = email!!.uid
                }
            }
        })
    }

    fun savingValueCheckBoxLike(model: String) {
        val refReceivingPoem =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                .child("MyFavouritesPoem")
        refReceivingPoem.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val children = p0.children
                    children.forEach {
                        val poem: PoemAnswer? = it.getValue(PoemAnswer::class.java)
                        arrayLike += poem!!.id

                    }
                    arrayLike.forEach {
                        if (model == it) {
                            viewState.workWithLike()
                        }
                    }
                }
            }
        })
    }
}