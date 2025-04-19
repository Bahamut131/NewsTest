package com.example.newstest.domain.repository

import com.example.newstest.domain.entity.NewsPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NewsRepository {

    fun loadNews(query : String) : Flow<List<NewsPost>>
    fun loadNewsWithCategory(category : String) : Flow<List<NewsPost>>

    suspend fun loadNextNews()
    suspend fun loadNextNewsWithCategory()

}