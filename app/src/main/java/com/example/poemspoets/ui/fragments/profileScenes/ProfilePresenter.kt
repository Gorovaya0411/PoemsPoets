package com.example.poemspoets.ui.fragments.profileScenes

import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.data.model.User
import com.example.poemspoets.ui.activities.mainScenes.MainActivity
import com.example.poemspoets.ui.dialogFragments.ForEmptyJobsDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import moxy.MvpPresenter

class ProfilePresenter : MvpPresenter<ProfileView>() {
    private var refUser: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null
    var array = emptyArray<String>()
    lateinit var username: String
    lateinit var uid: String
    var arrayCatalog = emptyArray<String>()
    var arrayPoem = emptyArray<String>()
    private val emptyMyJobsDialog = ForEmptyJobsDialog(::openAddActivity)

    fun addData() {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUser = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        refUser!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user: User? = p0.getValue(User::class.java)
                    viewState.showElementsProfile(user)
                    if (user!!.status == "") {
                        viewState.workWithStatus()
                    }
                    if (user.address == "") {
                        viewState.workWithAddress()
                    }
                    viewState.workWithAvatar(user)
                }


            }
        })
    }

    fun receivingPoem() {
        val refReceivingPoem =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                .child("MyJob")
        refReceivingPoem.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val children = p0.children
                    children.forEach {
                        val poem: PoemAnswer? = it.getValue(PoemAnswer::class.java)
                        array += poem!!.id
                    }
                    changeUsernameAll(username)
                }
            }
        })
    }

    private fun changeUsernameAll(model: String) {
        array.forEach {
            val refChangeUsernameMyJob =
                FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                    .child("MyJob")
                    .child(it)
                    .child("username")
            refChangeUsernameMyJob.setValue(model)
        }
    }

    fun receivingPoemCatalog() {
        val refReceivingPoem =
            FirebaseDatabase.getInstance().reference.child(uid).child("Poems")

        refReceivingPoem.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val children = p0.children
                    children.forEach {
                        val poem: PoemAnswer? = it.getValue(PoemAnswer::class.java)
                        arrayCatalog += poem!!.id
                    }
                    changeUsernameAllCatalog(username)
                }
            }
        })
    }

    private fun changeUsernameAllCatalog(model: String) {
        arrayCatalog.forEach {
            val refChangeUsernameCatalog =
                FirebaseDatabase.getInstance().reference.child(uid).child("Poems")
                    .child(it)
                    .child("username")
            refChangeUsernameCatalog.setValue(model)
        }
    }

    fun receivingPoemPoems() {
        val refReceivingPoem =
            FirebaseDatabase.getInstance().reference.child("Poem")

        refReceivingPoem.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val children = p0.children
                    children.forEach {
                        val poem: PoemAnswer? = it.getValue(PoemAnswer::class.java)
                        if (poem!!.uid == uid)
                            arrayPoem += poem.id
                    }
                    changeUsernameAllPoem(username)
                }
            }
        })
    }

    private fun changeUsernameAllPoem(model: String) {
        array.forEach {
            val refChangeUsernamePoem =
                FirebaseDatabase.getInstance().reference.child("Poem")
                    .child(it)
                    .child("username")
            refChangeUsernamePoem.setValue(model)
        }
    }

    fun checkListMyJob(model: MainActivity) {
        val refListMyJob =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                .child("MyJob")
        refListMyJob.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val poem: PoemAnswer? = p0.getValue(PoemAnswer::class.java)
                if (poem == null) {
                    viewState.showDialog(emptyMyJobsDialog)
                } else {
                    model.onActivityDetailedMyJab()
                }

            }
        })
    }

    private fun openAddActivity() {
        viewState.openAddActivity()
    }
}