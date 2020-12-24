package com.example.poemspoets.ui.fragments.profileScenes


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.poemspoets.ui.dialogFragments.ForOutDialog
import com.example.poemspoets.ui.activities.R
import com.example.poemspoets.data.model.User
import com.example.poemspoets.ui.activities.AddPoemActivity
import com.example.poemspoets.ui.activities.mainScenes.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.editTextUsername
import kotlinx.android.synthetic.main.fragment_profile.imageViewQuotationMarksDown
import kotlinx.android.synthetic.main.fragment_profile.imageViewQuotationMarksUp
import kotlinx.android.synthetic.main.fragment_profile.textViewStatus
import kotlinx.android.synthetic.main.fragment_profile.textViewUsername
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class ProfileFragment : MvpAppCompatFragment(), ProfileView {

    private val profilePresenter by moxyPresenter { ProfilePresenter() }

    private var firebaseUser: FirebaseUser? = null
    private val forOut = ForOutDialog(::logOut)

    lateinit var username: String
    lateinit var uid: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        progressBarAvatar.visibility = ProgressBar.INVISIBLE

        val mainActivity = activity as MainActivity

        imageViewNoInternet.visibility = ImageView.INVISIBLE
        textViewNoInternet.visibility = TextView.INVISIBLE
        val isConnected =
            mainActivity.lackInternet(imageViewNoInternet, textViewNoInternet)
        if (!isConnected) {
            ScrollView.visibility = android.widget.ScrollView.INVISIBLE
        }
        visibilityElementInitially(editTextUsername, imageViewDoneUp)
        visibilityElementInitially(editTextStatus, imageViewDone)
        visibilityElementInitially(editTextCommunication, imageViewDoneAddress)
        textViewInfoAll.visibility = TextView.INVISIBLE
        profilePresenter.addData()

        buttonChangePhoto.setOnClickListener {
            changePhotoUser()
        }


        buttonChangeUsername.setOnClickListener {
            visibilityElement(editTextUsername, imageViewDoneUp)
            textViewUsername.visibility = TextView.INVISIBLE
        }
        textViewStatus.setOnClickListener {
            visibilityElement(editTextStatus, imageViewDone)
            textViewStatus.visibility = TextView.INVISIBLE
        }
        textViewCommunication.setOnClickListener {
            visibilityElement(editTextCommunication, imageViewDoneAddress)
            textViewCommunication.visibility = TextView.INVISIBLE
            textViewInfoAll.visibility = TextView.VISIBLE
        }
        imageViewDoneAddress.setOnClickListener {
            changeAddress()
        }

        imageViewDone.setOnClickListener {
            changeStatus()

        }
        imageViewDoneUp.setOnClickListener {
            changeUsername()
        }

        buttonMyWorks.setOnClickListener {
            profilePresenter.checkListMyJob(mainActivity)
        }

        buttonOut.setOnClickListener {
            forOut.show(requireActivity().supportFragmentManager, "ForDeleteMyPoemDialog")
        }
    }

    override fun showDialog(model: DialogFragment) {
        model.show(
            requireActivity().supportFragmentManager,
            "ForEmptyJobsDialog"
        )
    }

    override fun showElementsProfile(model: User?) {
        textViewUsername.text = model!!.login
        textViewEmailUsers.text = model.email
        textViewStatus.text = model.status
        textViewCommunication.text = model.address
        uid = model.uid
    }

    override fun workWithStatus() {
        textViewStatus.text = "О чем вы сейчас думаете?"
        imageViewQuotationMarksUp.visibility = ImageView.INVISIBLE
        imageViewQuotationMarksDown.visibility = ImageView.INVISIBLE
    }

    override fun workWithAddress() {
        textViewCommunication.text = "Как с вами можно свзяаться?"
    }

    override fun workWithAvatar(model: User?) {
        Picasso.get()
            .load(model!!.avatar)
            .into(imageViewAvatar)
    }

    private fun visibilityElementInitially(model1: EditText, model2: ImageView) {
        model1.visibility = EditText.INVISIBLE
        model2.visibility = ImageView.INVISIBLE
    }

    private fun visibilityElement(model1: EditText, model2: ImageView) {
        model1.visibility = EditText.VISIBLE
        model2.visibility = ImageView.VISIBLE
    }

    private fun changeStatus() {
        val status = editTextStatus.text.toString()
        val refAddressUser =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                .child("status")
        val refStatusAll =
            FirebaseDatabase.getInstance().reference.child(uid).child("status")

        editTextStatus.visibility = EditText.INVISIBLE
        imageViewDone.visibility = ImageView.INVISIBLE
        textViewStatus.visibility = TextView.VISIBLE

        refAddressUser.setValue(status)
        refStatusAll.setValue(status)

        textViewStatus.text = status
        imageViewQuotationMarksUp.visibility = ImageView.VISIBLE
        imageViewQuotationMarksDown.visibility = ImageView.VISIBLE

        if (status == "") {
            imageViewQuotationMarksUp.visibility = ImageView.INVISIBLE
            imageViewQuotationMarksDown.visibility = ImageView.INVISIBLE
            textViewStatus.text = "О чем вы думаете сейчас?"
        }

        Toast.makeText(
            context,
            "Ваш статус успешно изменен!",
            Toast.LENGTH_LONG
        ).show()

    }

    private fun changeAddress() {

        val address = editTextCommunication.text.toString()
        val refAddressUser =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                .child("address")
        val refAddress =
            FirebaseDatabase.getInstance().reference.child(uid)
                .child("address")

        editTextCommunication.visibility = EditText.INVISIBLE
        textViewCommunication.visibility = TextView.VISIBLE
        imageViewDoneAddress.visibility = ImageView.INVISIBLE


        refAddressUser.setValue(address)
        refAddress.setValue(address)

        textViewCommunication.text = address
        textViewInfoAll.visibility = TextView.INVISIBLE

        if (address == "") {
            textViewCommunication.text = "Как с вами можно связаться?"
        }

        Toast.makeText(
            context,
            "Успешно изменено!",
            Toast.LENGTH_LONG
        ).show()

    }

    private fun logOut() {
        val mainActivity = activity as MainActivity
        mainActivity.logOut()
    }

    private fun changeUsername() {

        username = editTextUsername.text.toString()
        val refLogin =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                .child("login")
        profilePresenter.receivingPoem()
        profilePresenter.receivingPoemCatalog()
        profilePresenter.receivingPoemPoems()

        editTextUsername.visibility = EditText.INVISIBLE
        textViewUsername.visibility = TextView.VISIBLE
        imageViewDoneUp.visibility = ImageView.INVISIBLE

        if (username == "") {
            Toast.makeText(
                context,
                "Строка осталась пустой",
                Toast.LENGTH_LONG
            ).show()
        } else {
            refLogin.setValue(username)
            textViewUsername.text = username

            Toast.makeText(
                context,
                "Успешно изменено!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun openAddActivity() {
        val intent = Intent(requireContext(), AddPoemActivity::class.java)
        startActivity(intent)
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(700, 700)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start((activity as MainActivity))

    }
}








