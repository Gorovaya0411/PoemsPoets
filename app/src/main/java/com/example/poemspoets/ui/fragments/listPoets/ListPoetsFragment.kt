package com.example.poemspoets.ui.fragments.listPoets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.poemspoets.ui.activities.mainScenes.MainActivity
import com.example.poemspoets.ui.activities.R
import kotlinx.android.synthetic.main.fragment_list_poets.*


class ListPoetsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        return inflater.inflate(R.layout.fragment_list_poets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity

        imageViewNoInternet.visibility = ImageView.INVISIBLE
        textViewNoInternet.visibility = TextView.INVISIBLE

        mainActivity.lackInternet(imageViewNoInternet, textViewNoInternet)

        floatingActionButton.setOnClickListener {
            mainActivity.onActivityAddVerse()
        }

        NavigationUI.setupWithNavController(
            bottom_navigation_list_poets,
            Navigation.findNavController(
                mainActivity,
                R.id.nav_host_fragment_container_list_poets
            )
        )
    }
}
