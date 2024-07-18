package com.pascal.dansandroid.data.prefs

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.pascal.dansandroid.domain.model.User

object PreferencesLogin {

    private const val PREFS_NAME = "login_prefs"
    private const val IS_LOGIN = "is_login"
    private const val RESPONSE_LOGIN = "response_login"

    fun getIsLogin(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_LOGIN, false)
    }

    fun setIsLogin(
        context: Context,
        isLogin: Boolean
    ) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putBoolean(IS_LOGIN, isLogin)
            commit()
        }
    }

    fun setLoginResponse(context: Context, responseLogin: User?) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(RESPONSE_LOGIN, Gson().toJson(responseLogin))
            commit()
        }
    }

    fun getLoginResponse(context: Context): User? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val responseLoginJson = sharedPreferences.getString(RESPONSE_LOGIN, "")

        return if (responseLoginJson.isNullOrEmpty()) {
            null
        } else {
            Gson().fromJson(responseLoginJson, User::class.java)
        }
    }
}
