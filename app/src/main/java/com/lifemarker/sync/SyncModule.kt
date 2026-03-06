package com.lifemarker.sync

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {

    @Provides
    @Singleton
    fun provideGoogleDriveService(
        @ApplicationContext context: Context
    ): GoogleDriveService {
        return GoogleDriveService(context)
    }

    @Provides
    @Singleton
    fun provideGoogleDriveManager(
        @ApplicationContext context: Context,
        googleDriveService: GoogleDriveService
    ): GoogleDriveManager {
        return GoogleDriveManager(context, googleDriveService)
    }
}
