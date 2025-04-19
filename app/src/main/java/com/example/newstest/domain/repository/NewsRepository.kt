package com.example.newstest.domain.repository

import com.example.newstest.domain.entity.NewsPost
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun loadNews(query : String) : Flow<List<NewsPost>>
    fun loadNewsWithCategory(category : String) : Flow<List<NewsPost>>

}