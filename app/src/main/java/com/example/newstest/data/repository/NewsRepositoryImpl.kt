package com.example.newstest.data.repository

import android.util.Log
import com.example.newstest.data.local.db.NewsDao
import com.example.newstest.data.mapper.replaceSpaceWithPlus
import com.example.newstest.data.mapper.toNewsEverything
import com.example.newstest.data.mapper.toEntityListFromCategory
import com.example.newstest.data.mapper.toEntityListFromEverything
import com.example.newstest.data.mapper.toNewsCategory
import com.example.newstest.data.remote.ApiService
import com.example.newstest.domain.entity.NewsPost
import com.example.newstest.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val newsDao: NewsDao
) : NewsRepository {

    private var _listNewsEverything = mutableListOf<NewsPost>()
    private val listNewsEverything: List<NewsPost> get() = _listNewsEverything.toList()

    private var lastIdNewsEverything = 0
    private var oldQueryEverything: String = ""
    private val nextEverything = MutableSharedFlow<Unit>(replay = 1)

    override fun loadNews(query: String): Flow<List<NewsPost>> = flow {
        Log.d("NewsRepositoryImpl","Start work")
        if (oldQueryEverything.isBlank() || oldQueryEverything != query) {
            Log.d("NewsRepositoryImpl","Do remove")
            newsDao.deleteAllNewsEverything()
            _listNewsEverything.removeAll(_listNewsEverything)
            lastIdNewsEverything = 0
            Log.d("NewsRepositoryImpl","Get response")
            try {
                val response = apiService
                    .loadEverythingNews(query.replaceSpaceWithPlus())
                    .articles.map { it.toNewsEverything() }

                Log.d("NewsRepositoryImpl", "response = $response")
                newsDao.addNewsEverything(response)
                oldQueryEverything = query
            } catch (e: Exception) {
                Log.e("NewsRepositoryImpl", "Exception while loading news: ${e.message}", e)
            }
        }

        nextEverything.emit(Unit)
        nextEverything.collect {
            newsDao.loadNews(lastIdNewsEverything.takeIf { it != 0 } ?: 0).also {
                lastIdNewsEverything = it.last().id
                _listNewsEverything.addAll(it.toEntityListFromEverything())
                emit(listNewsEverything)
            }
        }

    }

    override suspend fun loadNextNews() {
        nextEverything.emit(Unit)
    }

    private var _listNewsCategory = mutableListOf<NewsPost>()
    private val listNewsCategory: List<NewsPost> get() = _listNewsCategory.toList()

    private var lastIdNewsCategory = 0
    private var oldQueryCategory: String = ""
    private val nextCategory = MutableSharedFlow<Unit>(replay = 1)

    override fun loadNewsWithCategory(category: String): Flow<List<NewsPost>> = flow {
        if (oldQueryCategory.isBlank() || oldQueryCategory != category) {
            newsDao.deleteAllNewsCategory()
            _listNewsCategory.removeAll(_listNewsCategory)
            lastIdNewsCategory = 0

            val response = apiService
                .loadCategoryNews(category.replaceSpaceWithPlus()).articles.map { it.toNewsCategory() }

            newsDao.addNewsCategory(response)
            oldQueryCategory = category
        }

        nextCategory.emit(Unit)
        nextCategory.collect {
            newsDao.loadNewsCategory(lastIdNewsCategory.takeIf { it != 0 } ?: 0).also {
                lastIdNewsCategory = it.last().id
                _listNewsCategory.addAll(it.toEntityListFromCategory())
                emit(listNewsCategory)
            }
        }
    }

    override suspend fun loadNextNewsWithCategory() {
        nextCategory.emit(Unit)
    }
}