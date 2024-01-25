package com.motivation.affirmations.data.di

import com.motivation.affirmations.data.repository.AffirmationRepository
import com.motivation.affirmations.data.repository.AffirmationRepositoryImpl
import com.motivation.affirmations.data.source.AffirmationDataSource
import com.motivation.affirmations.data.source.local.db.MotivationAppDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 17.02.2022.
 */
@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun providesRepository(
        db : MotivationAppDB,
        dataSource: AffirmationDataSource
    ): AffirmationRepository = AffirmationRepositoryImpl(db,dataSource)
}
