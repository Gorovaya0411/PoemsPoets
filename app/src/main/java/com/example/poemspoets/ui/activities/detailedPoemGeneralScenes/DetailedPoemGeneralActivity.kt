package com.example.poemspoets.ui.activities.detailedPoemGeneralScenes

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.activities.DetailedPoetAndUserActivity
import com.example.poemspoets.ui.activities.R
import com.example.poemspoets.ui.activities.mainScenes.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detailed_poem.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class DetailedPoemGeneralActivity : MvpAppCompatActivity(), DetailedPoemGeneralView {

    private val detailedPoemGeneralPresenter by moxyPresenter { DetailedPoemGeneralPresenter() }
    private lateinit var refMyPoem: DatabaseReference
    private lateinit var refFavouritesPoem: DatabaseReference
    private lateinit var getModelPoemAnswer: PoemAnswer
    private var firebaseUser: FirebaseUser? = null
    private var like: Int = 0
    private var checkActivity: Boolean = false
    private lateinit var idUser: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_poem)

        getModelPoemAnswer = intent.getSerializableExtra("KEY") as PoemAnswer
        like = getModelPoemAnswer.like
        like = getModelPoemAnswer.like
        textViewPoem.movementMethod = ScrollingMovementMethod()
        textViewTitlePoem.movementMethod = ScrollingMovementMethod()
        textViewGenre.text = getModelPoemAnswer.genre
        textViewPoem.text = getModelPoemAnswer.poem
        textViewTitlePoem.text = getModelPoemAnswer.titlePoem
        textViewTitlePoem.setOnClickListener { textViewTitlePoem.maxLines = Integer.MAX_VALUE }
        firebaseUser = FirebaseAuth.getInstance().currentUser

        val namePoetModified = getModelPoemAnswer.namePoet.replace("|", ".", true)

        detailedPoemGeneralPresenter.savingValueCheckBoxAddPoem(getModelPoemAnswer.id)
        detailedPoemGeneralPresenter.savingValueCheckBoxLike(getModelPoemAnswer.id)
        workWithToolbar()
        detailedPoemGeneralPresenter.addEmail()

        if (getModelPoemAnswer.namePoet == "") {
            buttonNamePoet.text = getModelPoemAnswer.username
            buttonNamePoet.setOnClickListener {
                openingNewActivity(getModelPoemAnswer)
            }
        } else {
            buttonNamePoet.text = namePoetModified
            buttonNamePoet.setOnClickListener {
                openingNewActivity(getModelPoemAnswer)
            }
        }

        checkBoxLike.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                if (!checkActivity) {
                    addFavouritesPoem()
                    like++
                    val refLike =
                        FirebaseDatabase.getInstance().reference.child("Poem")
                            .child(getModelPoemAnswer.id).child("like")
                    refLike.setValue(like)
                    val refLikeForMyJob =
                        FirebaseDatabase.getInstance().reference.child("Users")
                            .child(getModelPoemAnswer.uid).child("MyJob")
                            .child(getModelPoemAnswer.id).child("like")
                    refLikeForMyJob.setValue(like)

                    if (getModelPoemAnswer.namePoet != "") {
                        val refLikeForNamePoet =
                            FirebaseDatabase.getInstance().reference.child(getModelPoemAnswer.namePoet)
                                .child("Poems").child(getModelPoemAnswer.id).child("like")
                        refLikeForNamePoet.setValue(like)
                    } else {
                        val refLikeForUserName =
                            FirebaseDatabase.getInstance().reference.child(idUser)
                                .child("Poems").child(getModelPoemAnswer.id).child("like")
                        refLikeForUserName.setValue(like)
                    }
                }
            } else {
                like--
                checkActivity = false
                val refLike =
                    FirebaseDatabase.getInstance().reference.child("Poem")
                        .child(getModelPoemAnswer.id).child("like")
                refLike.setValue(like)
                val refDeleteFavouritesPoem =
                    FirebaseDatabase.getInstance().reference.child("Users")
                        .child(firebaseUser!!.uid).child("MyFavouritesPoem")
                        .child(getModelPoemAnswer.id)
                refDeleteFavouritesPoem.removeValue()
                val refLikeForMyJob =
                    FirebaseDatabase.getInstance().reference.child("Users")
                        .child(getModelPoemAnswer.uid).child("MyJob")
                        .child(getModelPoemAnswer.id).child("like")
                refLikeForMyJob.setValue(like)
                if (getModelPoemAnswer.namePoet != "") {
                    val refLikeForNamePoet =
                        FirebaseDatabase.getInstance().reference.child(getModelPoemAnswer.namePoet)
                            .child("Poems").child(getModelPoemAnswer.id).child("like")
                    refLikeForNamePoet.setValue(like)
                } else {
                    val refLikeForUserName =
                        FirebaseDatabase.getInstance().reference.child(idUser)
                            .child("Poems").child(getModelPoemAnswer.id).child("like")
                    refLikeForUserName.setValue(like)
                }
            }
        }

        checkBoxAddPoem.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                addMyAdded()
                Toast.makeText(
                    this,
                    "Уже добавлено!:)",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val refDeletePoem =
                    FirebaseDatabase.getInstance().reference.child("Users")
                        .child(firebaseUser!!.uid)
                        .child("MyAdded").child(getModelPoemAnswer.id)
                refDeletePoem.removeValue()
                Toast.makeText(
                    this,
                    "Удалено из добавленных:)",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun workWithToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = " "
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun workWithCheckbox() {
        checkBoxAddPoem.isChecked = true
    }

    override fun workWithLike() {
        checkActivity = true
        checkBoxLike.isChecked = true
        like = getModelPoemAnswer.like
    }


    private fun openingNewActivity(model: PoemAnswer) {
        val intent = Intent(this, DetailedPoetAndUserActivity::class.java)
        intent.putExtra("KEY", model)
        startActivity(intent)
    }

    private fun addFavouritesPoem() {
        refFavouritesPoem =
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(firebaseUser!!.uid).child("MyFavouritesPoem")
                .child(getModelPoemAnswer.id)

        val poemHashMap = HashMap<String, Any>()
        poemHashMap["id"] = getModelPoemAnswer.id

        refFavouritesPoem.updateChildren(poemHashMap)

    }

    private fun addMyAdded() {
        refMyPoem =
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(firebaseUser!!.uid).child("MyAdded")
                .child(getModelPoemAnswer.id)

        val poemHashMap = HashMap<String, Any>()
        poemHashMap["titlePoem"] = getModelPoemAnswer.titlePoem
        poemHashMap["namePoet"] = getModelPoemAnswer.namePoet
        poemHashMap["username"] = getModelPoemAnswer.username
        poemHashMap["genre"] = getModelPoemAnswer.genre
        poemHashMap["poem"] = getModelPoemAnswer.poem
        poemHashMap["id"] = getModelPoemAnswer.id
        poemHashMap["avatar"] = getModelPoemAnswer.avatar
        poemHashMap["like"] = getModelPoemAnswer.like

        refMyPoem.updateChildren(poemHashMap)

    }
}
