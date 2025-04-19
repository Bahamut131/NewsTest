package com.example.newstest.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newstest.data.local.model.NewsCategoryDbModel
import com.example.newstest.data.local.model.NewsEverythingDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewsEverything(listNewsEverythingDbModel: List<NewsEverythingDbModel>)

    @Query("DELETE FROM NewsEverything")
    suspend fun deleteAllNewsEverything()

    @Query("SELECT * FROM NewsEverything WHERE id > :id ORDER BY id ASC LIMIT 10")
    fun loadNews(id : Int) : Flow<List<NewsEverythingDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewsCategory(listNewsCategoryDbModel: List<NewsCategoryDbModel>)

    @Query("DELETE FROM NewsCategory")
    suspend fun deleteAllNewsCategory()

    @Query("SELECT * FROM NewsCategory WHERE id > :id ORDER BY id ASC LIMIT 10")
    fun loadNewsCategory(id : Int) : Flow<List<NewsCategoryDbModel>>

}