package com.pascal.dansandroid.di

import com.pascal.dansandroid.domain.usecase.RemoteUC
import org.koin.dsl.module

val domainModule = module {
    factory { RemoteUC(get()) }
}
