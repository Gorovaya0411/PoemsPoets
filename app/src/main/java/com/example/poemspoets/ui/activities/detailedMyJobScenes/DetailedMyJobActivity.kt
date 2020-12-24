package com.example.poemspoets.ui.activities.detailedMyJobScenes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.AdapterMyJob
import com.example.poemspoets.ui.activities.*
import kotlinx.android.synthetic.main.activity_detailed_my_job.*
import moxy.MvpAppCompatActivity

import moxy.ktx.moxyPresenter

class DetailedMyJobActivity : MvpAppCompatActivity(), DetailedMyJobView {

    private val detailedMyJobPresenter by moxyPresenter { DetailedMyJobPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_my_job)
        detailedMyJobPresenter.getData()
    }

    override fun openDeleteDialog(model: DialogFragment) {
        model.show(this.supportFragmentManager, "ForDeleteMyPoemDialog")
    }

    override fun openDialog(model: DialogFragment) {
        model.show(
            this@DetailedMyJobActivity.supportFragmentManager,
            "ForEmptyJobsDialog"
        )
    }

    override fun workWithAdapter(model: AdapterMyJob) {
        RecyclerMainMyJub.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        RecyclerMainMyJub.adapter = model
    }

    override fun openingNewActivity(model: PoemAnswer) {
        val intent = Intent(this, DetailedPoemForJobActivity::class.java)
        intent.putExtra("KEY", model)
        startActivity(intent)
    }

    override fun toast() {
        Toast.makeText(
            this,
            "Успешно удалено!",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun openAddActivity() {
        val intent = Intent(this, AddPoemActivity::class.java)
        startActivity(intent)
    }
}
