package com.example.newstest.data.repository

import com.example.newstest.data.local.db.NewsDao
import com.example.newstest.data.mapper.toNewsEverything
import com.example.newstest.data.mapper.toEntityList
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
        if (oldQueryEverything.isBlank() || oldQueryEverything != query) {
            newsDao.deleteAllNewsEverything()
            _listNewsEverything.removeAll(_listNewsEverything)
            lastIdNewsEverything = 0

            val response = apiService
                .loadEverythingNews(query).articles.map { it.toNewsEverything() }

            newsDao.addNewsEverything(response)
            oldQueryEverything = query
        }

        nextEverything.emit(Unit)
        nextEverything.collect {
            newsDao.loadNews(lastIdNewsEverything.takeIf { it != 0 } ?: 0).also {
                lastIdNewsEverything = it.last().id
                _listNewsEverything.addAll(it.toEntityList())
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
                .loadCategoryNews(category).articles.map { it.toNewsCategory() }

            newsDao.addNewsCategory(response)
            oldQueryCategory = category
        }

        nextCategory.emit(Unit)
        nextCategory.collect {
            newsDao.loadNewsCategory(lastIdNewsCategory.takeIf { it != 0 } ?: 0).also {
                lastIdNewsCategory = it.last().id
                _listNewsCategory.addAll(it.toEntityList())
                emit(listNewsCategory)
            }
        }
    }

    override suspend fun loadNextNewsWithCategory() {
        nextCategory.emit(Unit)
    }
}