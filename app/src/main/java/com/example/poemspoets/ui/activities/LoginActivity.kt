package com.example.poemspoets.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.poemspoets.ui.activities.mainScenes.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity() : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        workWithToolbar()

        buttonEntrance.setOnClickListener {
            loginUser()
        }

    }

    private fun loginUser() {
        val email: String = editTextEmail.text.toString()
        val password: String = editTextPassword.text.toString()

        when {
            email == "" -> Toast.makeText(this@LoginActivity, "Введите E-mail", Toast.LENGTH_LONG)
                .show()
            password == "" -> Toast.makeText(
                this@LoginActivity,
                "Введите пароль",
                Toast.LENGTH_LONG
            ).show()
            else ->
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent =
                                Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Ошибка:" + task.exception!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
        }
    }

    private fun workWithToolbar() {
        setSupportActionBar(toolbar_actionbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        toolbar_actionbar.setNavigationOnClickListener {
            val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
