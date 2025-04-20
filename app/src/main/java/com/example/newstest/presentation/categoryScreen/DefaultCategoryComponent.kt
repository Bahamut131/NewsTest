package com.example.newstest.presentation.categoryScreen

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

class DefaultCategoryComponent @AssistedInject constructor(
    private val store: CategoryStoreFactory,
    @Assisted("componentContext")private val componentContext: ComponentContext,
    @Assisted("onClickBack")private val onClickBack : () -> Unit
) : CategoryComponent, ComponentContext by componentContext{
    private val state = componentContext.instanceKeeper.getStore { store.create() }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CategoryStore.State>
        get() = state.stateFlow

    init {
        componentScope().launch {
            state.labels.collect {
                when(it){
                    CategoryStore.Label.OnBackClick -> onClickBack.invoke()
                }
            }
        }
    }

    override fun loadNewsByCategory(category: String) {
        state.accept(CategoryStore.Intent.ChangeCategory(category))
    }

    override fun loadCategoryNext() {
        state.accept(CategoryStore.Intent.LoadNextNews)
    }

    override fun onClickBack() {
        state.accept(CategoryStore.Intent.OnBackClick)
    }

    override fun onClickSearchNews() {
        state.accept(CategoryStore.Intent.OnSearchClick)
    }

    @AssistedFactory
    interface Factory{
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onClickBack") onClickBack : () -> Unit
        ) : DefaultCategoryComponent
    }
}