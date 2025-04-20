package com.example.newstest.presentation.categoryScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newstest.presentation.categoryScreen.category.CategoryName
import com.example.newstest.presentation.homeScreen.NewsCard

@Composable
fun CategoryContent(
    component: CategoryComponent
){
    val state by component.model.collectAsState()

    CategoryDropdown(categories = CategoryName.entries.map { it.category }) {
        component.loadNewsByCategory(it)
    }

    when(val category =state.categoryState){
        CategoryStore.State.CategoryState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text("Something do wrong :(")
            }
        }
        CategoryStore.State.CategoryState.Initial -> {

        }
        CategoryStore.State.CategoryState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        is CategoryStore.State.CategoryState.Success -> {
            LazyColumn {
                items(items = category.listNewsCategory, key ={it}){
                    NewsCard(it)
                }
                item{
                    if(category.isLoadNext){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }else{
                        SideEffect {
                            component.loadCategoryNext()
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun CategoryDropdown(
    categories: List<String>,
    modifier: Modifier = Modifier,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "") }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFE0E0E0))
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedCategory,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Arrow"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        selectedCategory = category
                        expanded = false
                        onCategorySelected(category)
                    }
                )
            }
        }
    }
}

