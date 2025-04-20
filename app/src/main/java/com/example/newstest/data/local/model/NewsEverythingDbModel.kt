package com.example.newstest.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NewsEverything")
data class NewsEverythingDbModel(
    @PrimaryKey(true)
    val id : Int = 0,
    val name: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
) {
}