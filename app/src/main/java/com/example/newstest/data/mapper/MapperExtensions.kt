package com.example.newstest.data.mapper

import com.example.bookshelf.Articles
import com.example.newstest.data.local.model.NewsCategoryDbModel
import com.example.newstest.data.local.model.NewsEverythingDbModel
import com.example.newstest.domain.entity.NewsPost

fun Articles.toNewsEverything() : NewsEverythingDbModel = NewsEverythingDbModel(
 name = this.source?.name ?: "",
 author = this.author ?: "",
 title = this.title ?: "",
 description = this.description ?: "",
 url = this.url ?: "",
 urlToImage = this.urlToImage ?: "",
 publishedAt = this.publishedAt ?: "",
 content = this.content ?: "",
)



fun Articles.toNewsCategory() : NewsCategoryDbModel = NewsCategoryDbModel(
    name = this.source?.name ?: "",
    author = this.author ?: "",
    title = this.title ?: "",
    description = this.description ?: "",
    url = this.url ?: "",
    urlToImage = this.urlToImage ?: "",
    publishedAt = this.publishedAt ?: "",
    content = this.content ?:""
)


fun NewsPost.toEntity() = NewsEverythingDbModel(
    id = this.id,
    name = this.name,
    author = this.author,
    title = this.title,
    description = this.description,
    url = this.url,
    urlToImage = this.urlToImage,
    publishedAt = this.publishedAt,
    content = this.content
)

fun NewsEverythingDbModel.toEntity() = NewsPost(
    id = this.id,
    name = this.name,
    author = this.author,
    title = this.title,
    description = this.description,
    url = this.url,
    urlToImage = this.urlToImage,
    publishedAt = this.publishedAt,
    content = this.content
)

fun List<NewsEverythingDbModel>.toEntityListFromEverything() : List<NewsPost>{
    return this.map { it.toEntity() }
}


fun NewsCategoryDbModel.toEntity() = NewsPost(
    id = this.id,
    name = this.name,
    author = this.author,
    title = this.title,
    description = this.description,
    url = this.url,
    urlToImage = this.urlToImage,
    publishedAt = this.publishedAt,
    content = this.content
)

fun List<NewsCategoryDbModel>.toEntityListFromCategory() : List<NewsPost>{
    return this.map { it.toEntity() }
}

fun String.replaceSpaceWithPlus() : String = this.replace(" ","+")