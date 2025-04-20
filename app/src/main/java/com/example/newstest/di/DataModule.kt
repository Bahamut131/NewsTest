package com.example.newstest.di

import android.content.Context
import com.example.newstest.data.local.db.NewsDao
import com.example.newstest.data.local.db.NewsDataBase
import com.example.newstest.data.remote.ApiFactory
import com.example.newstest.data.remote.ApiService
import com.example.newstest.data.repository.NewsRepositoryImpl
import com.example.newstest.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    fun bindNewsRepository(impl : NewsRepositoryImpl) : NewsRepository

    companion object{
        @ApplicationScope
        @Provides
        fun provideDataBase(
            context: Context
        ) : NewsDataBase = NewsDataBase.getInstance(context)

        @ApplicationScope
        @Provides
        fun provideNewsDao(
            dataBase: NewsDataBase
        ) : NewsDao = dataBase.newsDao()

        @ApplicationScope
        @Provides
        fun provideApiService() : ApiService = ApiFactory.apiService


    }

}