package com.motivation.affirmations.data.source.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.motivation.affirmations.data.source.local.db.entities.AffirmationEntity

@Dao
interface AffirmationDAO {
    @Query("SELECT * FROM affirmations")
    fun getAll(): List<AffirmationEntity>

    @Query("SELECT * FROM affirmations WHERE id = :id")
    fun get(id: Int): AffirmationEntity

    @Query("SELECT * FROM affirmations WHERE filename LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): List<AffirmationEntity>

    @Query("DELETE FROM affirmations")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg affirmation: AffirmationEntity)

    @Insert
    suspend fun insertAll(newsResources: List<AffirmationEntity>)

    @Delete
    fun delete(affirmation: AffirmationEntity)

    @Delete
    fun delete(affirmations: List<AffirmationEntity>)

    @Update
    fun update(affirmation: AffirmationEntity)

    @Update
    fun updateAll(affirmations: List<AffirmationEntity>)

    @Query("Update affirmations SET isPlayList = :isPlayList Where id in (:ids)")
    fun changeIsPlayList(ids: List<Int>, isPlayList: Boolean)
}
