package com.example.poemspoets.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.poemspoets.data.model.Bio
import com.example.poemspoets.data.model.PoemAnswer
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_additional_info.*
import kotlinx.android.synthetic.main.activity_add_additional_info.progressBar


class AddAdditionalInfoActivity : AppCompatActivity() {


    private lateinit var refStorageRoot: StorageReference
    private lateinit var namePoet: String
    var array = emptyArray<String>()
    private lateinit var receivedAvatar: String
    private lateinit var refBio: DatabaseReference
    private lateinit var getModel: PoemAnswer
    private lateinit var bio: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_additional_info)

        getModel = intent.getSerializableExtra("KEY") as PoemAnswer
        namePoet = getModel.namePoet
        refStorageRoot = FirebaseStorage.getInstance().reference
        progressBar.visibility = ProgressBar.INVISIBLE

        getAvatar()
        getBio()

        buttonAdd.setOnClickListener {
            addBio()
        }

        imageViewAvatar.setOnClickListener {
            changePhotoUser()
        }

    }

    private fun getAvatar() {
        val refBio = FirebaseDatabase.getInstance().reference.child("Poem").child(getModel.id)
        refBio.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val avatar: PoemAnswer? = p0.getValue(PoemAnswer::class.java)
                    receivedAvatar = avatar!!.avatar
                    if (receivedAvatar != "https://firebasestorage.googleapis.com/v0/b/poemspoets-9db16.appspot.com/o/icon.png?alt=media&token=3b87c7de-0a6f-48ab-8933-a72637c290ac") {
                        Picasso.get()
                            .load(receivedAvatar)
                            .into(imageViewAvatar)
                        textViewChangePhoto.visibility = TextView.INVISIBLE
                        imageViewAvatar.isEnabled = false
                    }
                }
            }
        })

    }

    private fun getBio() {

        val refBio = FirebaseDatabase.getInstance().reference.child(getModel.namePoet)
        refBio.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val biography: Bio? = p0.getValue(Bio::class.java)
                    bio = biography!!.biography
                    if (bio != "") {
                        editTextBiography.setText(bio)
                        editTextBiography.isEnabled = false
                    }
                }
            }
        })

    }

    private fun addBio() {
        val bio: String = editTextBiography.text.toString()
        val userHashMap = HashMap<String, Any>()
        userHashMap["biography"] = bio

        refBio =
            FirebaseDatabase.getInstance().reference.child(namePoet)

        refBio.updateChildren(userHashMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Эти данные будут добавлены!\nСпасибо)",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    val intent =
                        Intent(this, DetailedPoetAndUserActivity::class.java)
                    intent.putExtra("KEY", getModel)
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

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(700, 700)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(this)
    }

    private fun receivingPoem() {
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
                        if (poem!!.namePoet == namePoet) {
                            array += poem.id
                        }
                    }
                }
            }
        })
    }

    private fun changeAvatarAll(model: String) {
        array.forEach {
            val refChangeAvatarAll =
                FirebaseDatabase.getInstance().reference.child("Poem").child(it)
                    .child("avatar")
            refChangeAvatarAll.setValue(model)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        progressBar.visibility = ProgressBar.VISIBLE
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null
        ) {
            receivingPoem()

            val uri = CropImage.getActivityResult(data).uri
            val refAvatar = FirebaseDatabase.getInstance().reference.child(namePoet).child("avatar")
            val path = refStorageRoot.child("PhotoUser")
                .child(namePoet)
            path.putFile(uri).addOnCompleteListener { task1 ->
                if (task1.isSuccessful)
                    path.downloadUrl.addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            val photoUrl = task2.result.toString()
                            changeAvatarAll(photoUrl)
                            refAvatar.setValue(photoUrl)
                            Picasso.get()
                                .load(photoUrl)
                                .into(imageViewAvatar)
                            progressBar.visibility = ProgressBar.INVISIBLE

                            textViewChangePhoto.visibility = TextView.INVISIBLE
                        }
                    }
            }
        }
    }
}






