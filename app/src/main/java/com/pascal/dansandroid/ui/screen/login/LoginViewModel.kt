package com.pascal.dansandroid.ui.screen.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.pascal.dansandroid.data.prefs.PreferencesLogin
import com.pascal.dansandroid.domain.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {
    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?> = _user

    suspend fun signIn(context: Context, user: User) {
        delay(2000)
        _user.value = user
        PreferencesLogin.setIsLogin(context, true)
        PreferencesLogin.setLoginResponse(context, user)
    }
}