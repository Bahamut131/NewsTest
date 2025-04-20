package com.example.newstest.presentation.categoryScreen

import kotlinx.coroutines.flow.StateFlow

interface CategoryComponent {

    val model : StateFlow<CategoryStore.State>

    fun loadNewsByCategory(category : String)
    fun loadCategoryNext()
    fun onClickBack()
}