package com.example.newstest.presentation.rootComponent

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

class DefaultRootComponent : RootComponent {
    override val stack: Value<ChildStack<*, RootComponent.Child>>
        get() = TODO("Not yet implemented")
}