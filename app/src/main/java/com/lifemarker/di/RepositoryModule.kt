package com.lifemarker.di

import com.lifemarker.data.repository.CategoryRepositoryImpl
import com.lifemarker.data.repository.MarkerRepositoryImpl
import com.lifemarker.domain.repository.CategoryRepository
import com.lifemarker.domain.repository.MarkerRepository
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
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindMarkerRepository(
        markerRepositoryImpl: MarkerRepositoryImpl
    ): MarkerRepository
}
