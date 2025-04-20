package com.example.newstest.domain.usecase

import com.example.newstest.domain.repository.NewsRepository
import javax.inject.Inject

class LoadNewsUseCase @Inject constructor(private val repository: NewsRepository) {

    fun loadNews(query : String) = repository.loadNews(query)
    suspend fun loadNextNews() = repository.loadNextNews()

}