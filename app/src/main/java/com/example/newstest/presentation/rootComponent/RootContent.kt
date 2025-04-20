package com.example.newstest.presentation.rootComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.newstest.presentation.categoryScreen.CategoryContent
import com.example.newstest.presentation.homeScreen.HomeContent
import com.example.newstest.ui.theme.BottomBar
import com.example.newstest.ui.theme.NewsTestTheme

@Composable
fun RootContent(
    component: DefaultRootComponent
){
    val stack by component.stack.subscribeAsState()
    val currentChild = stack.active.instance

    NewsTestTheme {
        Scaffold (
            bottomBar = {
                NavigationBar(
                    containerColor = BottomBar
                ) {
                    NavigationBarItem(
                        selected = currentChild is RootComponent.Child.HomeComponentContent,
                        onClick = { component.navigateToHome() },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = currentChild is RootComponent.Child.CategoryComponentContent,
                        onClick = { component.navigateToCategory() },
                        icon = { Icon(Icons.Default.Category, contentDescription = "Categories") },
                        label = { Text("Categories") }
                    )
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Children(stack = component.stack) {
                    when (val instance = it.instance) {
                        is RootComponent.Child.HomeComponentContent -> {
                            HomeContent(instance.homeComponent)
                        }
                        is RootComponent.Child.CategoryComponentContent -> {
                            CategoryContent(instance.categoryComponent)
                        }
                    }
                }
            }
        }
    }

}