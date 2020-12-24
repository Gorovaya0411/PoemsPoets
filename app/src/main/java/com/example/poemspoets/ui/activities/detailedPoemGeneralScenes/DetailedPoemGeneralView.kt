package com.example.poemspoets.ui.activities.detailedPoemGeneralScenes

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface DetailedPoemGeneralView : MvpView {
    fun workWithCheckbox()
    fun workWithLike()
}