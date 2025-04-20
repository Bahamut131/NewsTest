package com.example.newstest.presentation.categoryScreen

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

class DefaultCategoryComponent(
    private val store: CategoryStore,
    private val componentContext: ComponentContext
) : CategoryComponent, ComponentContext by componentContext{
    override val model: StateFlow<CategoryStore.State>
        get() = TODO("Not yet implemented")

    override fun loadNewsByCategory(category: String) {
        TODO("Not yet implemented")
    }

    override fun loadCategoryNext() {
        TODO("Not yet implemented")
    }
}