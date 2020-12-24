package com.example.poemspoets.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.activities.mainScenes.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_detailed_poem_for_job.*

class DetailedPoemForJobActivity : AppCompatActivity() {
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_poem_for_job)

        val getModelPoemAnswer = intent.getSerializableExtra("KEY") as PoemAnswer
        val namePoetModified = getModelPoemAnswer.namePoet.replace("|", ".", true)
        val usernameModified = getModelPoemAnswer.username.replace("|", ".", true)

        textViewGenre.text = getModelPoemAnswer.genre
        textViewTitlePoem.movementMethod = ScrollingMovementMethod()
        textViewPoem.text = getModelPoemAnswer.poem
        textViewTitlePoem.text = getModelPoemAnswer.titlePoem
        textViewTitlePoem.setOnClickListener { textViewTitlePoem.maxLines = Integer.MAX_VALUE }
        firebaseUser = FirebaseAuth.getInstance().currentUser

        workWithToolbar()

        imageButton.setOnClickListener {
            openingChangeActivity(getModelPoemAnswer)
        }

        if (getModelPoemAnswer.namePoet == "") {
            buttonNamePoet.text = usernameModified
            buttonNamePoet.setOnClickListener {
                openingNewActivity(getModelPoemAnswer)
            }
        } else {
            buttonNamePoet.text = namePoetModified
            buttonNamePoet.setOnClickListener {
                openingNewActivity(getModelPoemAnswer)
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

    private fun openingNewActivity(model: PoemAnswer) {
        val intent = Intent(this, DetailedPoetAndUserActivity::class.java)
        intent.putExtra("KEY", model)
        startActivity(intent)
    }

    private fun openingChangeActivity(model: PoemAnswer) {
        val intent = Intent(this, ChangeYourPoemActivity::class.java)
        intent.putExtra("KEY", model)
        startActivity(intent)
    }


}
