package com.pascal.dansandroid.ui.screen.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pascal.dansandroid.R
import com.pascal.dansandroid.data.remote.dtos.ResponseDataItem
import com.pascal.dansandroid.domain.base.UiState
import com.pascal.dansandroid.ui.component.LoadingScreen
import com.pascal.dansandroid.ui.component.ShowErrorDialog
import com.pascal.dansandroid.ui.screen.DataViewModel
import com.pascal.dansandroid.ui.theme.Blue
import com.pascal.dansandroid.ui.theme.DansTheme
import com.pascal.dansandroid.utils.appendHtmlText
import com.pascal.dansandroid.utils.openWebPage
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    id: String = "",
    viewModel: DataViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showLoading by remember { mutableStateOf(false) }
    var showDialogError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var detailData by remember { mutableStateOf<ResponseDataItem?>(null) }
    val detailState by viewModel.detailState.collectAsState()

    BackHandler {
        onNavBack()
    }

    LaunchedEffect(Unit) {
        viewModel.loadDetail(id)
    }

    LaunchedEffect(detailState) {
        when (detailState) {
            is UiState.Empty -> {}
            is UiState.Loading -> {
                showLoading = true
            }

            is UiState.Error -> {
                val errorState = detailState as UiState.Error
                errorMessage = errorState.message
                showLoading = false
                showDialogError = true
            }

            is UiState.Success -> {
                showLoading = false
                val data = (detailState as UiState.Success).data
                detailData = data
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
        DetailContent(
            item = detailData,
            onNavBack = {
                onNavBack()
            }
        )
    }
}

@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    item: ResponseDataItem? = null,
    onNavBack: () -> Unit
) {
    val jobDescriptionAnnotatedString = buildAnnotatedString {
        appendHtmlText(item?.description?.trimIndent() ?: "No Description")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onNavBack() }
                    .size(24.dp),
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Job Detail",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Company",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        DetailHeader(
            item = item
        ) {

        }

        Text(
            text = "Job Spesification",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(12.dp)
        ) {
            Text(
                text = "Title",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item?.title ?: "No Title",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Fulltime",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (item?.type == "Full Time") "Yes" else "No",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Description",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = jobDescriptionAnnotatedString,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun DetailHeader(
    modifier: Modifier = Modifier,
    item: ResponseDataItem? = null,
    onDetail: (ResponseDataItem?) -> Unit
) {
    val context = LocalContext.current

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
                text = item?.company ?: "No Company",
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item?.location ?: "No Location",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp,
                    color = Color.Gray
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.clickable {
                    openWebPage(context, item?.companyUrl ?: "")
                },
                text = "Go to Website",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = Blue,
                    textDecoration = TextDecoration.Underline
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailPreview() {
    DansTheme {
        DetailContent(
            onNavBack = {}
        )
    }
}