package com.example.newstest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.example.newstest.NewsApp
import com.example.newstest.presentation.rootComponent.DefaultRootComponent
import com.example.newstest.presentation.rootComponent.RootContent
import com.example.newstest.ui.theme.NewsTestTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var defaultRootComponent: DefaultRootComponent.Factory

    private val component by lazy {
        (application as NewsApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val componentContext = defaultComponentContext()
        setContent {
            RootContent(defaultRootComponent.create(componentContext = componentContext))
        }
    }
}