package com.example.newstest.data.remote


import com.example.bookshelf.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("everything")
    suspend fun loadEverythingNews(
        @Query("q") query: String
    ) : NewsResponse

    @GET("top-headlines")
    suspend fun loadCategoryNews(
        @Query("category") category: String
    ) : NewsResponse

}