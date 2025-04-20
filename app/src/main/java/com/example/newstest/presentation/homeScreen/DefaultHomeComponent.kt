package com.example.newstest.presentation.homeScreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.newstest.presentation.core.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultHomeComponent @AssistedInject constructor(
    private val store: HomeStoreFactory,
    @Assisted("componentContext")private val componentContext: ComponentContext,
    @Assisted("onClickBack")private val onClickBack : () -> Unit
) : HomeComponent, ComponentContext by componentContext{

    private val state = componentContext.instanceKeeper.getStore { store.create() }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<HomeStore.State>
        get() = state.stateFlow

    init {
        componentScope().launch {
            state.labels.collect {
                when(it){
                    HomeStore.Label.OnBackClick -> onClickBack.invoke()
                }
            }
        }
    }

    override fun changeSearchQuery(category: String) {
        state.accept(HomeStore.Intent.ChangeSearchQuery(category))
    }

    override fun loadNewsNext() {
        state.accept(HomeStore.Intent.LoadNextNews)
    }

    override fun onClickSearchNews() {
        state.accept(HomeStore.Intent.OnSearchClick)
    }

    override fun onClickBack() {
        state.accept(HomeStore.Intent.OnBackClick)
    }


    @AssistedFactory
    interface Factory{
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onClickBack") onClickBack : () -> Unit
        ) : DefaultHomeComponent
    }
}