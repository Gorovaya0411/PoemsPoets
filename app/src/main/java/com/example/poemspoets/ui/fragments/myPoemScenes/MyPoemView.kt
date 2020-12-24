package com.example.poemspoets.ui.fragments.myPoemScenes

import androidx.fragment.app.DialogFragment
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.AdapterForMyPoem
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface MyPoemView : MvpView {
    fun showDialog(model: DialogFragment)
    fun showDeleteDialog(model: DialogFragment)
    fun openingNewActivity(model: PoemAnswer)
    fun openListPoemActivity()
    fun showToast()
    fun workWithAdapter(model: AdapterForMyPoem)
}