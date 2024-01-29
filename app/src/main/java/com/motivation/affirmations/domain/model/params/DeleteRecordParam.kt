package com.motivation.affirmations.domain.model.params

import com.motivation.affirmations.data.source.local.db.entities.AffirmationEntity
import com.motivation.affirmations.domain.usecases.core.UseCase

data class DeleteRecordParam(
    var affirmation: AffirmationEntity
) : UseCase.Params {
    fun deleted(): AffirmationEntity {
        affirmation.date = 0L
        affirmation.duration = ""
        affirmation.fileName = ""
        affirmation.filePath = ""
        return affirmation
    }
}
