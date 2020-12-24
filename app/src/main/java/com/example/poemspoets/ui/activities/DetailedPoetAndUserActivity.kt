package com.example.poemspoets.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poemspoets.data.model.Bio
import com.example.poemspoets.ui.dialogFragments.ForEmptyInfoDialog
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.data.model.User
import com.example.poemspoets.ui.AdapterForDetailedPoetAndUser
import com.example.poemspoets.ui.activities.detailedPoemGeneralScenes.DetailedPoemGeneralActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detailed_poet_and_user.*


class DetailedPoetAndUserActivity : AppCompatActivity() {

    private lateinit var getModel: PoemAnswer
    private lateinit var pathName: String
    private val emptyMyPoemDialog = ForEmptyInfoDialog(::openAddInfo)
    private var biog = ""
    private var address = ""
    private var status = ""
    private var uid = ""
    private var firebaseUser: FirebaseUser? = null
    private var listPoemPoet: MutableList<PoemAnswer?> = mutableListOf()
    private val myAdapter =
        AdapterForDetailedPoetAndUser { openingNewActivity(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_poet_and_user)

        getModel = intent.getSerializableExtra("KEY") as PoemAnswer

        val namePoetChanged = getModel.namePoet.replace("|", ".", true)
        val nameUsernameChanged = getModel.username.replace("|", ".", true)

        textViewStatus.movementMethod = ScrollingMovementMethod()
        Picasso.get()
            .load(getModel.avatar)
            .into(imageViewAvatar)
        textViewAddInfo.visibility = TextView.INVISIBLE
        imageViewAdd.visibility = ImageView.INVISIBLE
        firebaseUser = FirebaseAuth.getInstance().currentUser


        if (getModel.namePoet == "") {
            cardViewBio.visibility = CardView.INVISIBLE
            textViewUsernameOrNamePoet.text = nameUsernameChanged
            pathName = getModel.uid
            addInfoForUser()
        } else {
            getAvatarNew()
            textViewYourCommunication.visibility = TextView.INVISIBLE
            textViewUsernameOrNamePoet.text = namePoetChanged
            pathName = getModel.namePoet
            addBio()
        }

        workWithAdapter()
        getData()

        imageViewAdd.setOnClickListener {
            openAddInfo()
        }

        buttonBiography.setOnClickListener {
            val intent = Intent(this, BiographyActivity::class.java)
            intent.putExtra("KEY", biog)
            startActivity(intent)
            finish()
        }
    }

    private fun getAvatarNew() {
        val refAvatar = FirebaseDatabase.getInstance().reference.child("Poem").child(getModel.id)
        refAvatar.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val avatar: PoemAnswer? = p0.getValue(PoemAnswer::class.java)

                Picasso.get()
                    .load(avatar!!.avatar)
                    .into(imageViewAvatar)

            }
        })
    }

    private fun getData() {

        val refPoet = FirebaseDatabase.getInstance().reference.child(pathName).child("Poems")
        refPoet.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val children = p0.children
                children.forEach {
                    val poem: PoemAnswer? = it.getValue(PoemAnswer::class.java)
                    listPoemPoet.add(poem)
                }
                populateData(listPoemPoet)
            }
        })
    }

    private fun workWithAdapter() {
        RecyclerViewPoetAndUser.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        RecyclerViewPoetAndUser.adapter = myAdapter
    }

    private fun populateData(poems: MutableList<PoemAnswer?>) {
        myAdapter.setData(poems)
    }

    private fun openingNewActivity(model: PoemAnswer) {
        val intent = Intent(this, DetailedPoemGeneralActivity::class.java)
        intent.putExtra("KEY", model)
        startActivity(intent)
        finish()
    }

    private fun addBio() {
        val refBio = FirebaseDatabase.getInstance().reference.child(pathName)
        refBio.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val bio: Bio? = p0.getValue(Bio::class.java)
                    biog = bio!!.biography
                    convertData()
                }
            }
        })

    }

    private fun addInfoForUser() {
        val refInfo = FirebaseDatabase.getInstance().reference.child(getModel.uid)
        refInfo.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val info: User? = p0.getValue(User::class.java)
                    status = info!!.status
                    address = info.address
                    uid = info.uid
                    convertData()
                }
            }
        })

    }

    private fun openAddInfo() {
        val intent = Intent(this, AddAdditionalInfoActivity::class.java)
        intent.putExtra("KEY", getModel)
        startActivity(intent)
        finish()
    }

    private fun convertData() {
        if (getModel.namePoet == "") {
            textViewInfoAvailable.visibility = TextView.INVISIBLE
            imageViewAvatar.visibility = CardView.INVISIBLE
            if (status != "") {
                textViewStatus.text = status
            } else {
                textViewStatus.text = "Статус отсутствует"
            }
            if (address != "") {
                textViewCommunication.text = address
            } else {
                textViewCommunication.visibility = TextView.INVISIBLE
                textViewYourCommunication.visibility = TextView.INVISIBLE
            }

        } else {

            if (biog == "" && getModel.avatar == "https://firebasestorage.googleapis.com/v0/b/poemspoets-9db16.appspot.com/o/icon.png?alt=media&token=3b87c7de-0a6f-48ab-8933-a72637c290ac") {
                cardViewBio.visibility = CardView.INVISIBLE
                textViewAddInfo.visibility = TextView.VISIBLE
                imageViewAdd.visibility = ImageView.VISIBLE
                emptyMyPoemDialog.show(this.supportFragmentManager, "ForDeleteMyPoemDialog")
                textViewAddInfo.visibility = TextView.VISIBLE
                imageViewAdd.visibility = ImageView.VISIBLE
            } else if (getModel.avatar != "https://firebasestorage.googleapis.com/v0/b/poemspoets-9db16.appspot.com/o/icon.png?alt=media&token=3b87c7de-0a6f-48ab-8933-a72637c290ac" && biog == "") {
                textViewAddInfo.visibility = TextView.VISIBLE
                cardViewBio.visibility = CardView.INVISIBLE
                textViewInfoAvailable.text = "Биография отсуствует"
                imageViewAdd.visibility = ImageView.VISIBLE
            } else if (biog != "" && getModel.avatar == "https://firebasestorage.googleapis.com/v0/b/poemspoets-9db16.appspot.com/o/icon.png?alt=media&token=3b87c7de-0a6f-48ab-8933-a72637c290ac") {
                cardViewBio.visibility = CardView.VISIBLE
                textViewAddInfo.visibility = TextView.VISIBLE
                imageViewAdd.visibility = ImageView.VISIBLE
                textViewInfoAvailable.visibility = TextView.INVISIBLE
            } else {
                textViewInfoAvailable.visibility = TextView.INVISIBLE
                textViewAddInfo.visibility = TextView.INVISIBLE
                imageViewAdd.visibility = ImageView.INVISIBLE
                textViewAddInfo.visibility = TextView.INVISIBLE
                imageViewAdd.visibility = ImageView.INVISIBLE
            }
            textViewStatus.visibility = TextView.INVISIBLE
            imageViewQuotationMarksUp.visibility = ImageView.INVISIBLE
            imageViewQuotationMarksDown.visibility = ImageView.INVISIBLE
        }
    }

}
