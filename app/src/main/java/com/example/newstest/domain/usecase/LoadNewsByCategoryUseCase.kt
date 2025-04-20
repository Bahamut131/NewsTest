package com.example.newstest.domain.usecase

import com.example.newstest.domain.repository.NewsRepository
import javax.inject.Inject

class LoadNewsByCategoryUseCase @Inject constructor(private val repository: NewsRepository) {
     operator fun invoke(category : String) =
        repository.loadNewsWithCategory(category)
}