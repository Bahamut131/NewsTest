package com.example.newstest.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newstest.data.local.model.NewsCategoryDbModel
import com.example.newstest.data.local.model.NewsEverythingDbModel

@Database(entities = [NewsEverythingDbModel::class, NewsCategoryDbModel::class], version = 1, exportSchema = false)
abstract class NewsDataBase : RoomDatabase() {

    abstract fun newsDao() : NewsDao

    companion object{

        private const val DB_NAME = "News_Data_Base"
        private val LOCK: Any = Unit
        private var INSTANCE : NewsDataBase ?= null

        fun getInstance(context: Context) : NewsDataBase{
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }

                val db = Room
                    .databaseBuilder(
                        context=context,
                        name = DB_NAME,
                        klass = NewsDataBase::class.java)
                    .build()

                INSTANCE = db
                return db
            }
        }
    }


}