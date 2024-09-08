package com.carlostorres.di

import com.carlostorres.repository.HeroRepo
import com.carlostorres.repository.HeroRepoImpl
import org.koin.dsl.module

val koinModule = module {
    single<HeroRepo> {
        HeroRepoImpl()
    }
}