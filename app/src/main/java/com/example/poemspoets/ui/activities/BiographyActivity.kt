package com.example.poemspoets.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import kotlinx.android.synthetic.main.activity_biography.*

class BiographyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biography)

        val getModel = intent.getStringExtra("KEY")

        textViewBio.text = getModel
        textViewBio.movementMethod = ScrollingMovementMethod()
    }
}
