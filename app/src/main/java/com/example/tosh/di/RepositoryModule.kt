package com.example.tosh.di

import com.example.tosh.data.AppsRepositoryImpl
import com.example.tosh.domain.AppsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAppsRepository(impl: AppsRepositoryImpl): AppsRepository
}
