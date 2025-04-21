package com.example.newstest.presentation.categoryScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newstest.presentation.categoryScreen.category.CategoryName
import com.example.newstest.presentation.homeScreen.NewsCard
import com.example.newstest.ui.theme.BackGroundColor
import com.example.newstest.ui.theme.CardColor
import com.example.newstest.ui.theme.SearchColor

@Composable
fun CategoryContent(
    component: CategoryComponent
){
    val state by component.model.collectAsState()
    Column(
        modifier = Modifier.background(BackGroundColor)
    ) {
        CategoryDropdown(categories = CategoryName.entries.map { it.category }) {
            component.loadNewsByCategory(it)
        }
        when(val category =state.categoryState){
            CategoryStore.State.CategoryState.Error -> {
                Box(Modifier.fillMaxSize().background(BackGroundColor), contentAlignment = Alignment.Center){
                    Text("Something do wrong :(")
                }
            }
            CategoryStore.State.CategoryState.Initial -> {
                Box(Modifier.fillMaxSize().background(BackGroundColor), contentAlignment = Alignment.Center){

                }
            }
            CategoryStore.State.CategoryState.Loading -> {
                Box(Modifier.fillMaxSize().background(BackGroundColor), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }
            is CategoryStore.State.CategoryState.Success -> {
                LazyColumn(modifier = Modifier.background(BackGroundColor)) {
                    items(items = category.listNewsCategory, key ={it.id}){
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




}

@Composable
private fun CategoryDropdown(
    categories: List<String>,
    modifier: Modifier = Modifier,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "") }

    Box(modifier = modifier.background(SearchColor)) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(SearchColor)
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedCategory,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.fillMaxWidth().weight(1f)
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
                .fillMaxWidth()
                .background(SearchColor)
                .border(1.dp, CardColor)
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

