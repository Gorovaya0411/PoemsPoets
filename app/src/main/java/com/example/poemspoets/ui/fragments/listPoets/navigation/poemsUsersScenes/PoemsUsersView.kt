package com.example.poemspoets.ui.fragments.listPoets.navigation.poemsUsersScenes

import com.example.poemspoets.data.model.PoemAnswer
import com.example.poemspoets.ui.AdapterForUser
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface PoemsUsersView : MvpView {
    fun workWithSearchWidget(model: AdapterForUser)
    fun workWithAdapter(model: AdapterForUser)
    fun openingNewActivity(model: PoemAnswer)
}