package com.motivation.affirmations.data.di

import android.content.Context
import androidx.room.Room
import com.motivation.affirmations.data.source.local.db.AffirmationDAO
import com.motivation.affirmations.data.source.local.db.MotivationAppDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 17.02.2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRoomInstance(@ApplicationContext context: Context): MotivationAppDB =
        Room.databaseBuilder(context, MotivationAppDB::class.java, "MotivationDB.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideAudioRecorderDao(motivationDB: MotivationAppDB): AffirmationDAO =
        motivationDB.affirmationDAO()
}
