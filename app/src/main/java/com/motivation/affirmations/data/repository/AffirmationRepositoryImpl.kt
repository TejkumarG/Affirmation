package com.motivation.affirmations.data.repository

import com.motivation.affirmations.data.source.AffirmationDataSource
import com.motivation.affirmations.data.source.local.db.MotivationAppDB
import com.motivation.affirmations.data.source.local.db.entities.AffirmationEntity
import com.motivation.affirmations.data.source.mapper.asEntity
import com.motivation.affirmations.data.source.remote.api.model.AffirmationData
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AffirmationRepositoryImpl @Inject constructor(
    val db: MotivationAppDB,
    val source: AffirmationDataSource
) : AffirmationRepository {
    override suspend fun getAffirmations(): List<AffirmationEntity> {
        var affirmations: List<AffirmationEntity>
        withContext(Dispatchers.IO) {
            affirmations = db.affirmationDAO().getAll()
            if (affirmations.isEmpty()) {
                affirmations = source.getAffirmations().map(AffirmationData::asEntity)
                db.affirmationDAO().insertAll(affirmations)
            }
        }
        return affirmations
    }

    override suspend fun saveRecord(affirmation: AffirmationEntity): AffirmationEntity {
        return withContext(Dispatchers.IO) {
            db.affirmationDAO().update(affirmation)
            return@withContext db.affirmationDAO().get(affirmation.id)
        }
    }

    override suspend fun saveToPlaylist(ids: List<Int>) {
        withContext(Dispatchers.IO) {
            db.affirmationDAO().changeIsPlayList(ids, true)
        }
    }

    override suspend fun removeFromPlaylist(ids: List<Int>) {
        withContext(Dispatchers.IO) {
            db.affirmationDAO().changeIsPlayList(ids, false)
        }
    }

    override suspend fun updateRecords(
        affirmations: List<AffirmationEntity>
    ): List<AffirmationEntity> {
        return withContext(Dispatchers.IO) {
            db.affirmationDAO().updateAll(affirmations)
            db.affirmationDAO().getAll()
        }
    }

    override suspend fun getRecord(affirmationId: Int): AffirmationEntity {
        return withContext(Dispatchers.IO) {
            db.affirmationDAO().get(affirmationId)
        }
    }
}
