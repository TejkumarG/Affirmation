package com.motivation.affirmations.domain.model.params

import com.motivation.affirmations.data.source.local.db.entities.AffirmationEntity
import com.motivation.affirmations.domain.usecases.core.UseCase

data class SaveRecordParam(
    var fileName: String,
    var filePath: String,
    var date: Long,
    var duration: String,
    var affirmation: AffirmationEntity
) : UseCase.Params {
    fun updated(): AffirmationEntity {
        affirmation.fileName = fileName
        affirmation.filePath = filePath
        affirmation.date = date
        affirmation.duration = duration
        return affirmation
    }
}
