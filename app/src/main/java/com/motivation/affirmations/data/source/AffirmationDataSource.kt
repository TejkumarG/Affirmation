package com.motivation.affirmations.data.source

import com.motivation.affirmations.data.source.remote.api.model.AffirmationData

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 11.01.2024.
 */
interface AffirmationDataSource {
    suspend fun getAffirmations(): List<AffirmationData>
}
