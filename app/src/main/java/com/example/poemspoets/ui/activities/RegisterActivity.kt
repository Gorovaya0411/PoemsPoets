package com.example.poemspoets.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.example.poemspoets.ui.activities.mainScenes.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        progressBar.visibility = ProgressBar.INVISIBLE

        workWithToolbar()

        buttonRegistration.setOnClickListener {
            registerUser()

        }
    }

    private fun registerUser() {

        val login: String = editTextUsername.text.toString()
        val email: String = editTextEmail.text.toString()
        val password: String = editTextPassword.text.toString()
        val status = ""
        val address = ""

        when {
            login == "" ->
                Toast.makeText(this@RegisterActivity, "Введите имя пользователя", Toast.LENGTH_LONG)
                    .show()

            email == "" ->
                Toast.makeText(this@RegisterActivity, "Введите E-mail", Toast.LENGTH_LONG).show()
            password == "" ->
                Toast.makeText(this@RegisterActivity, "Введите пароль", Toast.LENGTH_LONG).show()
            else ->

                mAuth.createUserWithEmailAndPassword(email, password)

                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressBar.visibility = ProgressBar.VISIBLE
                            firebaseUserID = mAuth.currentUser!!.uid
                            refUsers = FirebaseDatabase.getInstance().reference.child("Users")
                                .child(firebaseUserID)

                            val userHashMap = HashMap<String, Any>()
                            userHashMap["uid"] = firebaseUserID
                            userHashMap["email"] = email
                            userHashMap["login"] = login
                            userHashMap["avatar"] =
                                "https://firebasestorage.googleapis.com/v0/b/poemspoets-9db16.appspot.com/o/icon.png?alt=media&token=3b87c7de-0a6f-48ab-8933-a72637c290ac"
                            userHashMap["search"] = login.toLowerCase()
                            userHashMap["status"] = status
                            userHashMap["address"] = address

                            refUsers.updateChildren(userHashMap)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {

                                        val intent =
                                            Intent(this@RegisterActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
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
            val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

