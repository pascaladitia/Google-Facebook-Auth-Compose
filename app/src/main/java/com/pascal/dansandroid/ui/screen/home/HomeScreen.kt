package com.pascal.dansandroid.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pascal.dansandroid.R
import com.pascal.dansandroid.data.remote.dtos.ResponseDataItem
import com.pascal.dansandroid.domain.base.UiState
import com.pascal.dansandroid.domain.model.ParamData
import com.pascal.dansandroid.ui.component.ButtonComponent
import com.pascal.dansandroid.ui.component.FormComponent
import com.pascal.dansandroid.ui.component.LoadingScreen
import com.pascal.dansandroid.ui.component.Search
import com.pascal.dansandroid.ui.component.ShowErrorDialog
import com.pascal.dansandroid.ui.screen.DataViewModel
import com.pascal.dansandroid.ui.theme.DansTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: DataViewModel = koinViewModel(),
    onDetail: (String) -> Unit
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isFulltime by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var isLocation by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var showFilter by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }
    var showDialogError by remember { mutableStateOf(false) }

    var searchData by remember { mutableStateOf<List<ResponseDataItem>?>(null) }

    val searchState by viewModel.searchState.collectAsState()
    val listData: LazyPagingItems<ResponseDataItem> =
        viewModel.listData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.loadListData()
    }

    LaunchedEffect(searchState) {
        when (searchState) {
            is UiState.Empty -> {}
            is UiState.Loading -> {
                showLoading = true
            }

            is UiState.Error -> {
                val errorState = searchState as UiState.Error
                errorMessage = errorState.message
                showLoading = false
                showDialogError = true
            }

            is UiState.Success -> {
                showLoading = false
                val data = (searchState as UiState.Success).data
                searchData = data
            }
        }
    }

    if (showLoading) {
        LoadingScreen()
    }

    ShowErrorDialog(
        title = "Dans Android",
        message = errorMessage,
        isDialogVisible = showDialogError
    ) {
        showDialogError = false
    }

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = {
                coroutineScope.launch {
                    viewModel.loadListData()
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center),
                        text = "Job List",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Search(
                        modifier = Modifier.weight(1f)
                    ) {
                        description = it
                        coroutineScope.launch {
                            viewModel.loadSearch(
                                ParamData(
                                    description = it,
                                    location = isLocation,
                                    full_time = isFulltime.toString()
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showFilter = !showFilter }
                            .size(24.dp),
                        imageVector = if (showFilter) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }

                AnimatedVisibility(showFilter) {
                    Column(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Fulltime",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.width(32.dp))

                            Switch(
                                modifier = Modifier
                                    .scale(0.8f),
                                checked = isFulltime,
                                onCheckedChange = {
                                    isFulltime = it
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Location",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.width(32.dp))

                            FormComponent(
                                value = isLocation,
                                onValueChange = {
                                    isLocation = it
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        ButtonComponent(text = "Apply Filter") {
                            coroutineScope.launch {
                                viewModel.loadSearch(
                                    ParamData(
                                        description = description,
                                        location = isLocation,
                                        full_time = isFulltime.toString()
                                    )
                                )
                            }
                            showFilter = false
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (searchData.isNullOrEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        state = rememberLazyListState()
                    ) {
                        items(count = listData.itemCount) { index ->
                            listData[index]?.let {
                                HomeItem(item = it) { item ->
                                    onDetail(item?.id.toString())
                                }
                            }
                        }

                        item {
                            if (showLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        listData.apply {
                            when {
                                loadState.refresh is LoadState.Loading -> {
                                    showLoading = true
                                }
                                loadState.append is LoadState.Loading -> {
                                    showLoading = false
                                }
                                loadState.append is LoadState.Error -> {
                                    showLoading = false
                                }
                                loadState.refresh is LoadState.NotLoading -> {
                                    showLoading = false
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Search Result",
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        items(searchData ?: emptyList()) { item ->
                            HomeItem(item = item) {
                                onDetail(it?.id.toString())
                            }
                        }
                    }
                }

            }
        }

    }
}

@Composable
fun HomeItem(
    modifier: Modifier = Modifier,
    item: ResponseDataItem? = null,
    onDetail: (ResponseDataItem?) -> Unit
) {
    Row(
        modifier = modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onDetail(item) }
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = item?.companyLogo)
                    .error(R.drawable.no_thumbnail)
                    .apply { crossfade(true) }
                    .build()
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .size(56.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item?.title ?: "No Title",
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item?.company ?: "No Company",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp,
                    color = Color.Gray
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item?.location ?: "No Location",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = Color.Gray
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onDetail(item) }
                .size(36.dp),
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    DansTheme {
        HomeItem() {

        }
    }
}