package com.example.poemspoets.ui.fragments.listPoets.navigation.poemsPoetsScenes

import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.AdapterForPoet
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface PoemsPoetsView : MvpView {
    fun workWithSearchWidget(model: AdapterForPoet)
    fun openingNewActivity(model: PoemAnswer)
    fun workWithAdapter(model: AdapterForPoet)
}