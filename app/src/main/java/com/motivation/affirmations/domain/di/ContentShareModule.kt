package com.motivation.affirmations.domain.di

import android.content.Context
import com.motivation.affirmations.util.ContentShareHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContentShareModule {

    @Provides
    @Singleton
    fun providesShareHelper(
        @ApplicationContext context: Context):ContentShareHelper = ContentShareHelper(context)
}