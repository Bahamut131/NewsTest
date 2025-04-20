package com.example.newstest.presentation.categoryScreen

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.newstest.presentation.categoryScreen.CategoryStore.Intent
import com.example.newstest.presentation.categoryScreen.CategoryStore.Label
import com.example.newstest.presentation.categoryScreen.CategoryStore.State

interface CategoryStore : Store<Intent, State, Label> {

    sealed interface Intent {
    }

    data class State()

    sealed interface Label {
    }
}

class CategoryStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): CategoryStore =
        object : CategoryStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CategoreStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
            }
    }
}
