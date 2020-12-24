package com.example.poemspoets.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.example.poemspoets.data.model.PoemAnswer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_detailed_poem_for_my_poem.buttonNamePoet
import kotlinx.android.synthetic.main.activity_detailed_poem_for_my_poem.textViewGenre
import kotlinx.android.synthetic.main.activity_detailed_poem_for_my_poem.textViewPoem
import kotlinx.android.synthetic.main.activity_detailed_poem_for_my_poem.textViewTitlePoem

class DetailedPoemForMyPoemActivity : AppCompatActivity() {

    lateinit var model: String
    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_poem_for_my_poem)

        val getModelPoemAnswer = intent.getSerializableExtra("KEY") as PoemAnswer
        val namePoetModified = getModelPoemAnswer.namePoet.replace("|", ".", true)
        val usernameModified = getModelPoemAnswer.username.replace("|", ".", true)

        textViewGenre.text = getModelPoemAnswer.genre
        textViewPoem.text = getModelPoemAnswer.poem
        textViewTitlePoem.movementMethod = ScrollingMovementMethod()
        textViewTitlePoem.text = getModelPoemAnswer.titlePoem
        textViewTitlePoem.setOnClickListener { textViewTitlePoem.maxLines = Integer.MAX_VALUE }
        firebaseUser = FirebaseAuth.getInstance().currentUser


        if (getModelPoemAnswer.namePoet == "") {
            buttonNamePoet.text = usernameModified
            buttonNamePoet.setOnClickListener {
                openingNewActivity(getModelPoemAnswer)
            }
        } else {
            buttonNamePoet.text = namePoetModified
            buttonNamePoet.setOnClickListener {
                model = getModelPoemAnswer.namePoet
                openingNewActivity(getModelPoemAnswer)
            }
        }
    }


    private fun openingNewActivity(model: PoemAnswer) {
        val intent = Intent(this, DetailedPoetAndUserActivity::class.java)
        intent.putExtra("KEY", model)
        startActivity(intent)
    }
}
