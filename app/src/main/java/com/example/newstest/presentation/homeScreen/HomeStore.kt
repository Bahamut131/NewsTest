package com.example.newstest.presentation.homeScreen

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.newstest.domain.entity.NewsPost
import com.example.newstest.domain.usecase.LoadNewsUseCase
import com.example.newstest.presentation.homeScreen.HomeStore.Intent
import com.example.newstest.presentation.homeScreen.HomeStore.Label
import com.example.newstest.presentation.homeScreen.HomeStore.State
import com.example.newstest.presentation.homeScreen.HomeStore.State.HomeState
import com.example.newstest.presentation.homeScreen.HomeStoreFactory.Msg.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

interface HomeStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangeSearchQuery(val query : String) : Intent

        data object OnSearchClick : Intent

        data object OnBackClick : Intent

        data object LoadNextNews : Intent

    }

    data class State(
        val query : String,
        val homeState : HomeState,
        val isLoadNext : Boolean
    ){
        sealed interface HomeState{

            data object Error: HomeState
            data object Loading: HomeState
            data object Initial: HomeState
            data class Success(val listNewsEverything : List<NewsPost>,val isLoadNext: Boolean): HomeState

        }
    }

    sealed interface Label {

        data object OnBackClick : Label

    }
}

class HomeStoreFactory(
    private val storeFactory: StoreFactory,
    private val loadNewsUseCase: LoadNewsUseCase
) {

    fun create(): HomeStore =
        object : HomeStore, Store<Intent, State, Label> by storeFactory.create(
            name = "HomeStore",
            initialState = State("", HomeState.Initial,false),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data class ChangeSearchQuery(val query: String) : Msg
        data object Error: Msg
        data object Loading: Msg
        data object IsLoadNext : Msg
        data class Success(val listNewsEverything : List<NewsPost>, val isLoadNext: Boolean): Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        private var searchJob : Job? = null
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent){
                is Intent.ChangeSearchQuery -> {
                    dispatch(ChangeSearchQuery(intent.query))
                }
                Intent.OnBackClick -> {
                    publish(Label.OnBackClick)
                }
                Intent.OnSearchClick -> {
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        try {
                            dispatch(Loading)
                            loadNewsUseCase.loadNews(getState.invoke().query).collect {
                                dispatch(Success(it,false))
                            }
                        }catch (_ : Exception){
                            dispatch(Error)
                        }
                    }
                }

                Intent.LoadNextNews -> {
                    scope.launch {
                        try {
                            loadNewsUseCase.loadNextNews()
                        }catch (_ : Exception){
                            dispatch(Error)
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is ChangeSearchQuery -> {
                    copy(query = message.query)
                }
                Error -> {
                    copy(homeState = HomeState.Error)
                }
                Loading -> {
                    copy(homeState = HomeState.Loading)
                }
                is Success -> {
                    copy(homeState = HomeState.Success(message.listNewsEverything,false))
                }

                IsLoadNext -> {
                    copy(isLoadNext = true)
                }
            }
    }
}



