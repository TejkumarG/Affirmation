package com.motivation.affirmations.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.motivation.affirmations.data.source.local.db.entities.AffirmationEntity

@Database(entities = [AffirmationEntity::class], version = 1)
abstract class MotivationAppDB: RoomDatabase() {
    abstract fun affirmationDAO(): AffirmationDAO
}