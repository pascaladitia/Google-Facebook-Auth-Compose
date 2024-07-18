package com.pascal.dansandroid.ui.screen.account

import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Announcement
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.pascal.dansandroid.R
import com.pascal.dansandroid.data.prefs.PreferencesLogin
import com.pascal.dansandroid.domain.model.User
import com.pascal.dansandroid.ui.component.ButtonComponent
import com.pascal.dansandroid.ui.theme.DansTheme

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val profile = PreferencesLogin.getLoginResponse(context)

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        AccountContent(
            data = profile
        ) {
            PreferencesLogin.setIsLogin(context, false)
            onLogout()
        }
    }
}

@Composable
fun AccountContent(
    modifier: Modifier = Modifier,
    data: User? = null,
    onLogout: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AccountHeader()

        Column(
            modifier = modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = Uri.parse(data?.photoUrl ?: ""))
                            .error(R.drawable.no_profile)
                            .apply { crossfade(true) }
                            .build()
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(56.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        modifier = Modifier,
                        text = data?.displayName ?: "No Name",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 14.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = data?.email ?: "No Email",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                        ),
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Feature",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                AccountMenu(
                    icons = Icons.Outlined.Person,
                    title = "Personal Information"
                ) {

                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                AccountMenu(
                    icons = Icons.Outlined.WorkOutline,
                    title = "Status Job"
                ) {

                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                AccountMenu(
                    icons = Icons.Outlined.Person,
                    title = "Notification"
                ) {

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                AccountMenu(
                    icons = Icons.Outlined.StarOutline,
                    title = "Rate Our App"
                ) {

                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                AccountMenu(
                    icons = Icons.Outlined.Settings,
                    title = "Settings"
                ) {

                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                AccountMenu(
                    icons = Icons.Outlined.EmojiEmotions,
                    title = "Reviews"
                ) {

                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                AccountMenu(
                    icons = Icons.AutoMirrored.Outlined.Announcement,
                    title = "About Us"
                ) {

                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            ButtonComponent(text = "Logout") {
                onLogout()
            }
        }

    }
}

@Composable
fun AccountHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Account",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AccountMenu(
    modifier: Modifier = Modifier,
    icons: ImageVector = Icons.Default.Home,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = icons,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(32.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Icon(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterEnd),
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountPreview() {
    DansTheme {
        AccountContent() {}
    }
}