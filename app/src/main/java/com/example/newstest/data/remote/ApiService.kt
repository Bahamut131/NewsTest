package com.example.newstest.data.remote


import com.example.bookshelf.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("everything")
    fun loadEverythingNews(
        @Query("q") query: String
    ) : NewsResponse

    @GET("top-headlines")
    fun loadCategoryNews(
        @Query("category") category: String
    ) : NewsResponse

}