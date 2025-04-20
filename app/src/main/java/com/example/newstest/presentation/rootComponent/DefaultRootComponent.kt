package com.example.newstest.presentation.rootComponent

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.newstest.presentation.categoryScreen.DefaultCategoryComponent
import com.example.newstest.presentation.homeScreen.DefaultHomeComponent
import com.example.newstest.presentation.rootComponent.RootComponent.Child
import com.example.newstest.presentation.rootComponent.RootComponent.Child.CategoryComponentContent
import com.example.newstest.presentation.rootComponent.RootComponent.Child.HomeComponentContent
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    private val defaultHomeComponent: DefaultHomeComponent.Factory,
    private val defaultCategoryComponent: DefaultCategoryComponent.Factory,
    private val componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext{

    private val navigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.HomeScreenConfig,
        handleBackButton = true,
        childFactory = ::child,
        serializer = Config.serializer()
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ) : Child{
        when(config){
            Config.CategoryScreenConfig -> {

                val component = defaultCategoryComponent.create(
                    componentContext = componentContext,
                    onClickBack = { navigation.pop() }
                )

                return CategoryComponentContent(component)
            }
            Config.HomeScreenConfig -> {
                val component = defaultHomeComponent.create(
                    componentContext = componentContext,
                    onClickBack = {navigation.pop()}
                )
                return HomeComponentContent(component)
            }

        }
    }


    fun navigateToHome() {
        navigation.push(Config.HomeScreenConfig)
    }

    fun navigateToCategory() {
        navigation.push(Config.CategoryScreenConfig)
    }

    @Serializable
    sealed interface Config{
        data object HomeScreenConfig : Config
        data object CategoryScreenConfig : Config
    }
}