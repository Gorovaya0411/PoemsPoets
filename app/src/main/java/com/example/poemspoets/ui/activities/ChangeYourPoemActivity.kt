package com.example.poemspoets.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.poemspoets.data.model.Bio
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.activities.detailedMyJobScenes.DetailedMyJobActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_change_your_poem.*
import kotlinx.android.synthetic.main.activity_change_your_poem.autoCompleteTextView
import kotlinx.android.synthetic.main.activity_change_your_poem.editTextGenre
import kotlinx.android.synthetic.main.activity_change_your_poem.editTextPoem
import kotlinx.android.synthetic.main.activity_change_your_poem.editTextTitlePoem
import kotlinx.android.synthetic.main.activity_change_your_poem.textViewNamePoet

class ChangeYourPoemActivity : AppCompatActivity() {

    private var firebaseUserID: String = ""
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refPoemPoetOrUser: DatabaseReference
    private lateinit var getModelPoemAnswer: PoemAnswer
    var array: MutableList<String> = mutableListOf()
    private lateinit var namePoet: String
    private lateinit var namePoetModified: String
    private lateinit var refAllPoem: DatabaseReference
    private lateinit var refJob: DatabaseReference
    private lateinit var getAvatar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_your_poem)

        getModelPoemAnswer = intent.getSerializableExtra("KEY") as PoemAnswer
        mAuth = FirebaseAuth.getInstance()

        receivingNamePoet()
        editTextTitlePoem.setText(getModelPoemAnswer.titlePoem)

        namePoetModified = getModelPoemAnswer.namePoet.replace("|", ".", true)

        if (getModelPoemAnswer.namePoet == "") {
            autoCompleteTextView.visibility = AutoCompleteTextView.INVISIBLE
            textViewNamePoet.text = getModelPoemAnswer.username
        } else {
            textViewNamePoet.visibility = AutoCompleteTextView.INVISIBLE
            autoCompleteTextView.setText(namePoetModified)
        }
        editTextGenre.setText(getModelPoemAnswer.genre)
        editTextPoem.setText(getModelPoemAnswer.poem)

        buttonReady.setOnClickListener {
            changePoem()
        }
    }

    private fun changePoem() {
        firebaseUserID = mAuth.currentUser!!.uid
        val titlePoem = editTextTitlePoem.text.toString()
        val genre = editTextGenre.text.toString()
        val poem = editTextPoem.text.toString()
        namePoet = autoCompleteTextView.text.toString()
        val namePoetModified = namePoet.replace(".", "|", true)


        refAllPoem =
            FirebaseDatabase.getInstance().reference.child("Poem").child(getModelPoemAnswer.id)
        refJob = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
            .child("MyJob").child(getModelPoemAnswer.id)

        refPoemPoetOrUser = if (namePoetModified == "") {
            FirebaseDatabase.getInstance().reference.child(firebaseUserID).child("Poems")
                .child(getModelPoemAnswer.id)

        } else {
            FirebaseDatabase.getInstance().reference.child(namePoetModified).child("Poems")
                .child(getModelPoemAnswer.id)
        }
        if (getModelPoemAnswer.namePoet != namePoet) {
            FirebaseDatabase.getInstance().reference.child(getModelPoemAnswer.namePoet)
                .child("Poems").child(getModelPoemAnswer.id).removeValue()
            addAvatar()
        }
        val poemHashMap = HashMap<String, Any>()
        poemHashMap["titlePoem"] = titlePoem
        poemHashMap["namePoet"] = namePoetModified
        poemHashMap["username"] = getModelPoemAnswer.username
        poemHashMap["genre"] = genre
        poemHashMap["poem"] = poem
        poemHashMap["id"] = getModelPoemAnswer.id
        poemHashMap["uid"] = getModelPoemAnswer.uid
        poemHashMap["like"] = getModelPoemAnswer.like

        refAllPoem.updateChildren(poemHashMap)
        refJob.updateChildren(poemHashMap)
        refPoemPoetOrUser.updateChildren(poemHashMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Ваш стих изменен!:)",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    val intent =
                        Intent(this, DetailedMyJobActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Ошибка:" + it.exception!!.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun receivingNamePoet() {
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
                        if (poem!!.namePoet != "") {
                            val namePoetModified = poem.namePoet.replace("|", ".", true)
                            array.add(namePoetModified)
                            val set: Set<String> = HashSet(array)
                            array.clear()
                            array.addAll(set)
                        }
                        val adapter = ArrayAdapter(
                            this@ChangeYourPoemActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            array
                        )
                        autoCompleteTextView.setAdapter(adapter)
                        autoCompleteTextView.threshold = 1
                        autoCompleteTextView.onItemClickListener =
                            AdapterView.OnItemClickListener { parent, _,
                                                              position, _ ->
                                val selectedItem = parent.getItemAtPosition(position).toString()

                                namePoet = selectedItem
                            }

                    }
                }
            }
        })
    }

    private fun addAvatar() {
        namePoet = autoCompleteTextView.text.toString()
        namePoetModified = namePoet.replace(".", "|", true)
        val refUser =
            FirebaseDatabase.getInstance().reference.child(namePoetModified)
        refUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val avatar: Bio? = p0.getValue(Bio::class.java)
                    getAvatar = avatar!!.avatar
                }
                val poemHashMap = HashMap<String, Any>()
                if (getAvatar == "") {
                    poemHashMap["avatar"] =
                        "https://firebasestorage.googleapis.com/v0/b/poemspoets-9db16.appspot.com/o/icon.png?alt=media&token=3b87c7de-0a6f-48ab-8933-a72637c290ac"
                } else {
                    poemHashMap["avatar"] =
                        getAvatar
                }

                refJob.updateChildren(poemHashMap)
                refAllPoem.updateChildren(poemHashMap)
                refPoemPoetOrUser.updateChildren(poemHashMap)
            }
        })

    }
}
