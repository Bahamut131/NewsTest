package com.example.newstest

import android.app.Application
import com.example.newstest.di.DaggerApplicationComponent

class NewsApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(
            this
        )
    }

}