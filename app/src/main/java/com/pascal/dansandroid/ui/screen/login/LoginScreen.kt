package com.pascal.dansandroid.ui.screen.login

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.auth
import com.pascal.dansandroid.R
import com.pascal.dansandroid.domain.model.User
import com.pascal.dansandroid.ui.component.ShowErrorDialog
import com.pascal.dansandroid.ui.theme.DansTheme
import com.pascal.dansandroid.utils.AuthResultContract
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: LoginViewModel = koinViewModel(),
    onLogin: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var text by remember { mutableStateOf<String?>(null) }
    val user by remember { viewModel.user }.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var showDialogError by remember { mutableStateOf(false) }
    val signInRequestCode = 1

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
            try {
                isLoading = false

                val account = task?.getResult(ApiException::class.java)
                if (account == null) {
                    text = "Google sign in failed"
                    showDialogError = true
                } else {
                    coroutineScope.launch {
                        viewModel.signIn(
                            context,
                            User(
                                email = account.email ?: "",
                                displayName = account.displayName ?: "",
                                photoUrl = (account.photoUrl ?: "").toString()
                            )
                        )
                    }
                }
            } catch (e: ApiException) {
                isLoading = false
                text = "Google sign in failed"
                showDialogError = true
            }
        }

    user?.let {
        onLogin()
    }

    BackHandler {
        (context as? ComponentActivity)?.finish()
    }

    ShowErrorDialog(
        title = "Dans Android",
        message = text ?: "",
        isDialogVisible = showDialogError
    ) {
        showDialogError = false
    }

    Surface(
        modifier = modifier.padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            SignInButton(
                text = "Sign in with Google",
                loadingText = "Signing in...",
                isLoading = isLoading,
                icon = painterResource(id = R.drawable.google),
                onClick = {
                    isLoading = true
                    authResultLauncher.launch(signInRequestCode)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "OR",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            SigninFacebookButton(
                onSignedIn = {
                    coroutineScope.launch {
                        viewModel.signIn(context, it)
                    }
                },
                onSignInFailed = {
                    text = "Facebook sign in failed"
                    showDialogError = true
                }
            )
        }
    }
}

@Composable
fun SignInButton(
    text: String,
    loadingText: String = "Signing in...",
    icon: Painter,
    isLoading: Boolean = false,
    borderColor: Color = Color.LightGray,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(
            enabled = !isLoading,
            onClick = onClick
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = icon,
                contentDescription = "SignInButton",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isLoading) loadingText else text,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = textColor
                )
            )

            if (isLoading) {

                Spacer(modifier = Modifier.width(16.dp))

                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
        }
    }
}

@Composable
fun SigninFacebookButton(
    onSignInFailed: (Exception) -> Unit,
    onSignedIn: (User) -> Unit,
) {
    val scope = rememberCoroutineScope()
    AndroidView({ context ->
        LoginButton(context).apply {
            val callbackManager = CallbackManager.Factory.create()
            setPermissions("email", "public_profile")
            registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    // do nothing
                }

                override fun onError(error: FacebookException) {
                    onSignInFailed(error)
                }

                override fun onSuccess(result: LoginResult) {
                    scope.launch {
                        val token = result.accessToken.token
                        val credential = FacebookAuthProvider.getCredential(token)
                        val authResult = Firebase.auth.signInWithCredential(credential).await()
                        if (authResult.user != null) {
                            onSignedIn(
                                User(
                                    email = authResult.user!!.email ?: "",
                                    displayName = authResult.user!!.displayName ?: "",
                                    photoUrl = (authResult.user!!.photoUrl ?: "").toString()
                                )
                            )
                        } else {
                            onSignInFailed(RuntimeException("Could not sign in with Firebase"))
                        }
                    }
                }
            })
        }
    })
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    DansTheme {

    }
}