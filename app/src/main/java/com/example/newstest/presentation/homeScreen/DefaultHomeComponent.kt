package com.example.newstest.presentation.homeScreen

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

class DefaultHomeComponent(
    private val store: HomeStore,
    private val componentContext: ComponentContext
) : HomeComponent, ComponentContext by componentContext{
    override val model: StateFlow<HomeStore.State>
        get() = TODO("Not yet implemented")

    override fun loadNewsByCategory(category: String) {
        TODO("Not yet implemented")
    }

    override fun loadCategoryNext() {
        TODO("Not yet implemented")
    }
}