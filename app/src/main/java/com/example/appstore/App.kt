package com.example.appstore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import coil.compose.AsyncImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.google.accompanist.pager.calculateCurrentOffsetForPage


@OptIn(ExperimentalPagerApi::class)
@Composable
fun App(navController: NavController) {

    val viewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("App Store")
                    },
                    backgroundColor =  MaterialTheme.colorScheme.background
                )
            }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val scope = rememberCoroutineScope()
                val pagerState = rememberPagerState()
                val pages = listOf(
                    "For you",
                    "Top charts",
                    "Kids"
                )
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    backgroundColor = MaterialTheme.colorScheme.background,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier
                                .fillMaxWidth()
                                .pagerTabIndicatorOffset(pagerState, tabPositions)
                        )
                    }
                ) {
                    pages.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch{
                                pagerState.animateScrollToPage(index)
                                }
                            }
                        )

                    }
                }
                HorizontalPager(
                    count = pages.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->

                    val pageOffset = calculateCurrentOffsetForPage(page)
                    val scale = 1f - kotlin.math.abs(pageOffset) * 0.2f
                    val alpha = 1f - kotlin.math.abs(pageOffset) * 0.5f
                    val rotation = pageOffset * 10f

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                this.alpha = alpha
                                rotationY = rotation
                                cameraDistance = 12f * density
                            }
                    ) {
                        when (page) {
                            0 -> ForYouTab(viewModel, navController)
                            1 -> TopChartsTab(viewModel, navController)
                            2 -> KidsTab(viewModel, navController)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TopChartsTab(viewModel: ViewModel, navController: NavController) {

    val sortedAppList by viewModel.sortedAppList.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sortedAppList) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val encodedName = URLEncoder.encode(
                            item.name,
                            StandardCharsets.UTF_8.toString()
                        )
                        val encodedSubject = URLEncoder.encode(
                            item.subject,
                            StandardCharsets.UTF_8.toString()
                        )
                        val encodedpackageName = URLEncoder.encode(
                            item.packageName,
                            StandardCharsets.UTF_8.toString()
                        )
                        val encodedimageUrl = URLEncoder.encode(
                            item.imageUrl,
                            StandardCharsets.UTF_8.toString()
                        )
                        navController.navigate("details/$encodedName/${item.downloads}/$encodedSubject/${item.kids}/$encodedpackageName/$encodedimageUrl")
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {

                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = "App Image",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 12.dp)
                    )
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${item.downloads} downloads",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.subject,
                            style = MaterialTheme.typography.bodySmall
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun KidsTab(viewModel: ViewModel, navController: NavController) {

    val kidsItems by viewModel.kidsAppList.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(kidsItems) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val encodedName = URLEncoder.encode(
                            item.name,
                            StandardCharsets.UTF_8.toString()
                        )
                        val encodedSubject = URLEncoder.encode(
                            item.subject,
                            StandardCharsets.UTF_8.toString()
                        )
                        val encodedpackageName = URLEncoder.encode(
                            item.packageName,
                            StandardCharsets.UTF_8.toString()
                        )
                        val encodedimageUrl = URLEncoder.encode(
                            item.imageUrl,
                            StandardCharsets.UTF_8.toString()
                        )
                        navController.navigate("details/$encodedName/${item.downloads}/$encodedSubject/${item.kids}/$encodedpackageName/$encodedimageUrl")
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {

                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = "App Image",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 12.dp)
                    )
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${item.downloads} downloads",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.subject,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ForYouTab(viewModel: ViewModel, navController: NavController) {

    val AppList by viewModel.AppList.collectAsState()

    val subjects by viewModel.subjects.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(0.dp, 20.dp, 0.dp, 50.dp)
    ) {
        items(subjects) { subject ->
            if (subject != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    subject?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }

                    val itemsForSubject = AppList.filter { it.subject == subject }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(itemsForSubject.chunked(2)) { itemPair ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.width(200.dp)
                            ) {
                                itemPair.forEach { item ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)
                                            .clickable {
                                                val encodedName = URLEncoder.encode(
                                                    item.name,
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                                val encodedSubject = URLEncoder.encode(
                                                    item.subject,
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                                val encodedpackageName = URLEncoder.encode(
                                                    item.packageName,
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                                val encodedimageUrl = URLEncoder.encode(
                                                    item.imageUrl,
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                                navController.navigate("details/$encodedName/${item.downloads}/$encodedSubject/${item.kids}/$encodedpackageName/$encodedimageUrl")
                                            },
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp)
                                        ) {

                                            AsyncImage(
                                                model = item.imageUrl,
                                                contentDescription = "App Image",
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .padding(top = 10.dp)
                                            )
                                            Column(modifier = Modifier.padding(10.dp)) {
                                                Text(
                                                    text = item.name,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Text(
                                                    text = "${item.downloads} downloads",
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
