package com.example.poemspoets.ui.fragments.myPoemScenes


import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.AdapterForMyPoem
import com.example.poemspoets.ui.activities.DetailedPoemForMyPoemActivity
import com.example.poemspoets.ui.activities.mainScenes.MainActivity
import com.example.poemspoets.ui.activities.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_my_poem.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class MyPoemFragment : MvpAppCompatFragment(), MyPoemView {

    private val myPoemPresenter by moxyPresenter { MyPoemPresenter() }

    private var firebaseUser: FirebaseUser? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        return inflater.inflate(R.layout.fragment_my_poem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        imageViewNoInternet.visibility = ImageView.INVISIBLE
        textViewNoInternet.visibility = TextView.INVISIBLE

        mainActivity.lackInternet(imageViewNoInternet, textViewNoInternet)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        myPoemPresenter.getData()
    }

    override fun showDialog(model: DialogFragment) {
        model.show(
            requireActivity().supportFragmentManager,
            "ForEmptyJobsDialog"
        )
    }

    override fun showDeleteDialog(model: DialogFragment) {
        model.show(requireActivity().supportFragmentManager, "ForDeleteMyPoemDialog")
    }


    override fun openingNewActivity(model: PoemAnswer) {
        val intent = Intent(context, DetailedPoemForMyPoemActivity::class.java)
        intent.putExtra("KEY", model)
        startActivity(intent)

    }

    override fun workWithAdapter(model: AdapterForMyPoem) {
        RecyclerMainMyPoem.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        RecyclerMainMyPoem.adapter = model
    }


    override fun openListPoemActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    override fun showToast() {
        Toast.makeText(
            context,
            "Успешно удалено!",
            Toast.LENGTH_LONG
        ).show()
    }
}
