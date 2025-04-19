package com.example.newstest.data.repository

import com.example.newstest.domain.entity.NewsPost
import com.example.newstest.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(): NewsRepository {
    override fun loadNews(query: String): Flow<List<NewsPost>> {
        TODO("Not yet implemented")
    }

    override fun loadNewsWithCategory(category: String): Flow<List<NewsPost>> {
        TODO("Not yet implemented")
    }
}