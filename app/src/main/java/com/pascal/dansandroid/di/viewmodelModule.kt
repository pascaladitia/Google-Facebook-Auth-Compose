package com.pascal.dansandroid.di

import com.pascal.dansandroid.ui.screen.DataViewModel
import com.pascal.dansandroid.ui.screen.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { DataViewModel(get()) }
}

