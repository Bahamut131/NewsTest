package com.example.newstest.domain.entity

data class NewsPost(
    val id : Int = UNSPECIFIED_ID,
    val name: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
) {

    companion object{
        const val UNSPECIFIED_ID = 0
    }
}