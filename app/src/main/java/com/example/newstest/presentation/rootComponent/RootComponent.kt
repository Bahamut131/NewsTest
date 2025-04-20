package com.example.newstest.presentation.rootComponent

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.newstest.presentation.categoryScreen.DefaultCategoryComponent
import com.example.newstest.presentation.homeScreen.DefaultHomeComponent

interface RootComponent {

    val stack : Value<ChildStack<*, Child>>

    sealed interface Child{

        data class HomeComponentContent(val homeComponent: DefaultHomeComponent) : Child
        data class CategoryComponentContent(val categoryComponent: DefaultCategoryComponent) : Child

    }

}