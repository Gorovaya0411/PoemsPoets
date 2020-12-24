package com.example.poemspoets.ui.activities.mainScenes

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.poemspoets.ui.activities.AddPoemActivity
import com.example.poemspoets.ui.activities.R
import com.example.poemspoets.ui.activities.WelcomeActivity
import com.example.poemspoets.ui.activities.detailedMyJobScenes.DetailedMyJobActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import kotlin.random.Random

class MainActivity : MvpAppCompatActivity(), MainView {

    private val mainPresenter by moxyPresenter { MainPresenter() }

    private lateinit var mAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    private lateinit var refStorageRoot: StorageReference
    private var refChangeAvatarInUser: DatabaseReference? = null
    var array = emptyArray<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        NavigationUI.setupWithNavController(
            bottom_navigation,
            Navigation.findNavController(
                this,
                R.id.nav_host_fragment_container
            )
        )

        mAuth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refStorageRoot = FirebaseStorage.getInstance().reference
        refChangeAvatarInUser =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                .child("avatar")


    }

    fun logOut() {
        mAuth.signOut()
        val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun onActivityAddVerse() {
        val intent = Intent(this@MainActivity, AddPoemActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun lackInternet(model: ImageView, model1: TextView): Boolean {
        val connectionManager: ConnectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectionManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (!isConnected) {

            model.visibility = ImageView.VISIBLE
            model1.visibility = TextView.VISIBLE
            val images = arrayOf(
                R.drawable.no_internet_one,
                R.drawable.no_internet_two,
                R.drawable.no_internet_three,
                R.drawable.no_internet_for,
                R.drawable.no_internet_five,
                R.drawable.no_internet_six,
                R.drawable.no_internet_seven,
                R.drawable.no_internet_eight,
                R.drawable.no_internet_nine,
                R.drawable.no_internet_ten
            )
            val rand = Random.nextInt(9)
            val random = images[rand]
            model.setImageResource(random)
        }
        return isConnected
    }

    fun onActivityDetailedMyJab() {
        val intent = Intent(this@MainActivity, DetailedMyJobActivity::class.java)
        startActivity(intent)
        finish()

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
        progressBarAvatar.visibility = ProgressBar.VISIBLE
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null
        ) {
            mainPresenter.receivingName()

            val uri = CropImage.getActivityResult(data).uri

            val path = refStorageRoot.child("PhotoUser")
                .child(firebaseUser!!.uid)
            path.putFile(uri).addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    path.downloadUrl.addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            val photoUrl = task2.result.toString()
                            refChangeAvatarInUser!!.setValue(photoUrl)
                            changeAvatarAll(photoUrl)
                            Picasso.get()
                                .load(photoUrl)
                                .into(imageViewAvatar)
                            progressBarAvatar.visibility = ProgressBar.INVISIBLE
                        }
                    }

                }
            }
        }
    }
}

