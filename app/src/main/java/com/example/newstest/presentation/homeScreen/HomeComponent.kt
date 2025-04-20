package com.example.newstest.presentation.homeScreen

import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {

    val model : StateFlow<HomeStore.State>

    fun changeSearchQuery(query : String)
    fun loadNewsNext()
    fun onClickSearchNews()
    fun onClickBack()

}