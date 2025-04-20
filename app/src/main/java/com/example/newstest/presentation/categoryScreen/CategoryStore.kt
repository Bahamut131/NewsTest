package com.example.newstest.presentation.categoryScreen

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.newstest.domain.entity.NewsPost
import com.example.newstest.domain.usecase.LoadNewsByCategoryUseCase
import com.example.newstest.presentation.categoryScreen.CategoryStore.Intent
import com.example.newstest.presentation.categoryScreen.CategoryStore.Label
import com.example.newstest.presentation.categoryScreen.CategoryStore.State
import com.example.newstest.presentation.categoryScreen.CategoryStore.State.CategoryState
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface CategoryStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeCategory(val category : String) : Intent

        data object OnBackClick : Intent

        data object LoadNextNews : Intent
    }

    data class State(
        val category : String,
        val categoryState : CategoryState,
        val isLoadNext : Boolean
    ){
        sealed interface CategoryState{

            data object Error: CategoryState
            data object Loading: CategoryState
            data object Initial: CategoryState
            data class Success(val listNewsCategory : List<NewsPost>,val isLoadNext: Boolean): CategoryState

        }
    }

    sealed interface Label {
        data object OnBackClick : Label
    }
}

class CategoryStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val loadNewsByCategoryUseCase: LoadNewsByCategoryUseCase
) {

    fun create(): CategoryStore =
        object : CategoryStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CategoryStore",
            initialState = State(
                "", CategoryState.Initial,
                isLoadNext = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action


    private sealed interface Msg {
        data class ChangeCategory(val category: String) : Msg
        data object Error: Msg
        data object Loading: Msg
        data object IsLoadNext : Msg
        data class Success(val listNewsCategory : List<NewsPost>, val isLoadNext: Boolean): Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        private var searchJob : Job? = null
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent){
                is Intent.ChangeCategory -> {
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        try {
                            dispatch(Msg.ChangeCategory(intent.category))
                            dispatch(Msg.Loading)
                            loadNewsByCategoryUseCase.loadNewsWithCategory(getState.invoke().category).collect {
                                dispatch(Msg.Success(it,false))
                            }
                        }catch (_ : Exception){
                            dispatch(Msg.Error)
                        }
                    }
                }
                Intent.LoadNextNews -> {
                    scope.launch {
                        try {
                            dispatch(Msg.IsLoadNext)
                            loadNewsByCategoryUseCase.loadNextCategory()
                        }catch (_ : Exception){
                            dispatch(Msg.Error)
                        }
                    }
                }
                Intent.OnBackClick -> {
                    publish(Label.OnBackClick)
                }

            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.ChangeCategory -> {
                    copy(category = message.category)
                }
                Msg.Error -> {
                    copy(categoryState = CategoryState.Error)
                }
                Msg.IsLoadNext -> {
                    copy(isLoadNext = true)
                }
                Msg.Loading -> {
                    copy(categoryState = CategoryState.Loading)
                }
                is Msg.Success -> {
                    copy(categoryState = CategoryState.Success(message.listNewsCategory,false))
                }
            }
    }
}
