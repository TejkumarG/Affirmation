package com.motivation.affirmations.data.repository

import com.motivation.affirmations.data.source.local.db.entities.AffirmationEntity

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 11.01.2024.
 */
interface AffirmationRepository {
    suspend fun getAffirmations() : List<AffirmationEntity>

    suspend fun saveRecord(affirmation: AffirmationEntity) : AffirmationEntity

    suspend fun saveToPlaylist(ids : List<Int>)

    suspend fun removeFromPlaylist(ids: List<Int>)

    suspend fun updateRecords(affirmations: List<AffirmationEntity>) :  List<AffirmationEntity>

    suspend fun getRecord(affirmationId: Int) : AffirmationEntity
}
