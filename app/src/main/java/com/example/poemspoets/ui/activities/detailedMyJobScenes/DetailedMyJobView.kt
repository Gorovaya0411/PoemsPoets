package com.example.poemspoets.ui.activities.detailedMyJobScenes

import androidx.fragment.app.DialogFragment
import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.AdapterMyJob
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface DetailedMyJobView : MvpView {
    fun openDialog(model: DialogFragment)
    fun openAddActivity()
    fun openingNewActivity(model: PoemAnswer)
    fun openDeleteDialog(model: DialogFragment)
    fun toast()
    fun workWithAdapter(model: AdapterMyJob)
}