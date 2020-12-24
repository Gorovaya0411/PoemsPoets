package com.example.poemspoets.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.poemspoets.data.model.Bio
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.data.model.User
import com.example.poemspoets.ui.activities.mainScenes.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_poem.*


class AddPoemActivity : AppCompatActivity() {

    private lateinit var refAllPoem: DatabaseReference
    private lateinit var refPoemUsers: DatabaseReference
    private lateinit var refPoemPoetOrUser: DatabaseReference
    private lateinit var login: String
    private lateinit var refBio: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    lateinit var uid: String
    lateinit var avatar: String
    var array: MutableList<String> = mutableListOf()
    private lateinit var namePoet: String
    private var getAvatar: String? = null
    private lateinit var namePoetModified: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_poem)

        mAuth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        textViewNamePoet.visibility = TextView.INVISIBLE

        workWithToolbar()
        receivingNamePoet()
        addUser()

        checkBox.setOnCheckedChangeListener { _, isChecked ->

            if (!isChecked) {
                textViewNamePoet.visibility = TextView.INVISIBLE
                autoCompleteTextView.visibility = AutoCompleteTextView.VISIBLE
            } else {
                autoCompleteTextView.visibility = AutoCompleteTextView.INVISIBLE
                autoCompleteTextView.setText("")
                textViewNamePoet.visibility = TextView.VISIBLE
                textViewNamePoet.text = login

            }
        }

        buttonAdd.setOnClickListener {
            addPoem()
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
                    val adapter = ArrayAdapter(
                        this@AddPoemActivity, android.R.layout.simple_dropdown_item_1line, array
                    )
                    children.forEach {
                        val poem: PoemAnswer? = it.getValue(PoemAnswer::class.java)
                        if (poem!!.namePoet != "") {
                            val namePoetModified = poem.namePoet.replace("|", ".", true)
                            array.add(namePoetModified)
                            val set: Set<String> = HashSet(array)
                            array.clear()
                            array.addAll(set)
                        }

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

    private fun addPoem() {

        val titlePoem = editTextTitlePoem.text.toString()
        val genre = editTextGenre.text.toString()
        val poem = editTextPoem.text.toString()
        val username = login

        refAllPoem = FirebaseDatabase.getInstance().reference.child("Poem").push()
        refPoemUsers =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                .child("MyJob")
                .child(refAllPoem.key.toString())
        namePoet = autoCompleteTextView.text.toString()
        namePoetModified = namePoet.replace(".", "|", true)

        if (titlePoem == "") {
            Toast.makeText(this@AddPoemActivity, "Введите название стиха", Toast.LENGTH_LONG)
                .show()
        } else if (genre == "") {
            Toast.makeText(this@AddPoemActivity, "Введите жанр", Toast.LENGTH_LONG).show()
        } else if (poem == "") {
            Toast.makeText(this@AddPoemActivity, "Введите стих", Toast.LENGTH_LONG).show()
        } else if (namePoetModified == "" && !checkBox.isChecked) {
            Toast.makeText(
                this@AddPoemActivity,
                "Введите имя автора",
                Toast.LENGTH_LONG
            ).show()
        } else {
            addAvatar()
            if (namePoetModified == "") {
                refPoemPoetOrUser =
                    FirebaseDatabase.getInstance().reference.child(uid).child("Poems")
                        .child(refAllPoem.key.toString())

            } else {
                refPoemPoetOrUser =
                    FirebaseDatabase.getInstance().reference.child(namePoetModified).child("Poems")
                        .child(refAllPoem.key.toString())
                refBio = FirebaseDatabase.getInstance().reference.child(namePoetModified)
                    .child("biography")
                refBio.setValue("")
            }


            val poemHashMap = HashMap<String, Any>()
            poemHashMap["titlePoem"] = titlePoem
            poemHashMap["namePoet"] = namePoetModified
            poemHashMap["username"] = username
            poemHashMap["genre"] = genre
            poemHashMap["poem"] = poem
            poemHashMap["id"] = refAllPoem.key.toString()
            poemHashMap["uid"] = uid
            poemHashMap["like"] = 0

            refPoemUsers.updateChildren(poemHashMap)
            refAllPoem.updateChildren(poemHashMap)
            refPoemPoetOrUser.updateChildren(poemHashMap)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            this@AddPoemActivity,
                            "Ваш стих добавлен!:)",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        val intent =
                            Intent(this, MainActivity::class.java)
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
    }

    private fun workWithToolbar() {
        setSupportActionBar(toolbarAddPoem)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        toolbarAddPoem.setNavigationOnClickListener {
            val intent = Intent(this@AddPoemActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun addAvatar() {
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
                if (namePoet == "") {
                    poemHashMap["avatar"] = avatar
                } else {
                    if (getAvatar == "") {
                        poemHashMap["avatar"] =
                            "https://firebasestorage.googleapis.com/v0/b/poemspoets-9db16.appspot.com/o/icon.png?alt=media&token=3b87c7de-0a6f-48ab-8933-a72637c290ac"
                    } else {
                        poemHashMap["avatar"] =
                            getAvatar!!
                    }
                }
                refPoemUsers.updateChildren(poemHashMap)
                refAllPoem.updateChildren(poemHashMap)
                refPoemPoetOrUser.updateChildren(poemHashMap)
            }
        })
    }


    private fun addUser() {

        val refUser =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        refUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user: User? = p0.getValue(User::class.java)
                    login = user!!.login
                    uid = user.uid
                    avatar = user.avatar
                }
            }
        })
    }
}
