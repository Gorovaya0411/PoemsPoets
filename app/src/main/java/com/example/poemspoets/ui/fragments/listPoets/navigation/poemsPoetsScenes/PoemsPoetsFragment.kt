package com.example.poemspoets.ui.fragments.listPoets.navigation.poemsPoetsScenes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.AdapterForPoet
import com.example.poemspoets.ui.activities.detailedPoemGeneralScenes.DetailedPoemGeneralActivity
import com.example.poemspoets.ui.activities.R
import kotlinx.android.synthetic.main.fragment_poems_poets.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter


class PoemsPoetsFragment : MvpAppCompatFragment(), PoemsPoetsView {

    private val poemsPoetsPresenter by moxyPresenter { PoemsPoetsPresenter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poems_poets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        poemsPoetsPresenter.getData()
    }

    override fun workWithSearchWidget(model: AdapterForPoet) {
        country_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                model.getFilter().filter(newText)
                return false
            }

        })
    }

    override fun openingNewActivity(model: PoemAnswer) {
        val intent = Intent(context, DetailedPoemGeneralActivity::class.java)
        intent.putExtra("KEY", model)
        startActivity(intent)
    }


    override fun workWithAdapter(model: AdapterForPoet) {
        RecyclerMain.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        RecyclerMain.adapter = model
    }


}





