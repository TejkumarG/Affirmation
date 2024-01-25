package com.motivation.affirmations.data.di

import android.content.Context
import com.motivation.affirmations.data.source.AffirmationDataSource
import com.motivation.affirmations.data.source.remote.api.fake.AppAssetManager
import com.motivation.affirmations.data.source.remote.api.fake.FakeNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 17.02.2022.
 */
@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {

    @Qualifier
    annotation class Local

    @Qualifier
    annotation class Remote

    @Provides
    @Singleton
    fun providesAppAssetManager(
        @ApplicationContext context: Context,
    ): AppAssetManager = AppAssetManager(context.assets::open)

    @Provides
    @Singleton
    fun providesNetworkDataSource(
        assetManager: AppAssetManager
    ): AffirmationDataSource = FakeNetworkDataSource(assetManager)
}
