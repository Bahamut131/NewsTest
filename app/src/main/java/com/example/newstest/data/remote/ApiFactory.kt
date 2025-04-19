package com.example.newstest.data.remote

import com.example.newstest.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object ApiFactory {

    const val BASE_URL = "https://newsapi.org/v2/"
    const val PAGE_SIZE = "pageSize"
    const val SORT_BY = "sortBy"
    const val API_KEY = "apiKey"

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain->
            val oldRequest = chain.request()
            val newUrl = oldRequest.url.newBuilder()
                .addQueryParameter(API_KEY, BuildConfig.NEWS_API_KEY)
                .addQueryParameter(PAGE_SIZE,"50")
                .addQueryParameter(SORT_BY,"popularity")
                .build()
            val newRequest = oldRequest.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val apiService : ApiService = retrofit.create()
}