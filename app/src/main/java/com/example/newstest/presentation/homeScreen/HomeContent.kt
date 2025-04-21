package com.example.newstest.presentation.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.newstest.domain.entity.NewsPost
import com.example.newstest.ui.theme.BackGroundColor
import com.example.newstest.ui.theme.CardColor
import com.example.newstest.ui.theme.SearchColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    component: HomeComponent,
){
    val state by component.model.collectAsState()
    SearchBar(
        colors = SearchBarDefaults.colors(containerColor = BackGroundColor),
        inputField = {
            InputField(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .border(width = 1.dp, color = CardColor)
                    .background(SearchColor),
                query = state.query,
                onQueryChange = { component.changeSearchQuery(it) },
                onSearch = {
                    component.onClickSearchNews()
                },
                expanded = true,
                onExpandedChange = {},
                leadingIcon = {
                    IconButton(onClick = {
                        component.onClickBack()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { component.onClickSearchNews() }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "")
                    }
                }
            )
        },
        expanded = true,
        onExpandedChange = {},
    ) {
        when (val searchState = state.homeState) {
            HomeStore.State.HomeState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("Something do wrong :(")
                }
            }
            HomeStore.State.HomeState.Initial -> {}
            HomeStore.State.HomeState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }

            is HomeStore.State.HomeState.Success -> {
                if(searchState.listNewsEverything.isEmpty()){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text("Not found any news by query")
                    }
                }
                LazyColumn {
                    items(items = searchState.listNewsEverything, key ={it.id}){
                        NewsCard(it)
                    }
                    item{
                        if(searchState.isLoadNext){
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
                                component.loadNewsNext()
                            }
                        }
                    }
                }
            }


        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsCard(news: NewsPost, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors().copy(containerColor = CardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            GlideImage(
                model = news.urlToImage,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "by ${news.author.ifBlank { "Unknown" }} • ${news.publishedAt.take(10)}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
            )

            Text(
                text = news.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}