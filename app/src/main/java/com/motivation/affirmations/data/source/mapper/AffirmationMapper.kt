package com.motivation.affirmations.data.source.mapper

import com.motivation.affirmations.data.source.local.db.entities.AffirmationEntity
import com.motivation.affirmations.data.source.remote.api.model.AffirmationData
import com.motivation.affirmations.domain.model.Affirmation

fun Affirmation.toEntity() = AffirmationEntity(
    id = id,
    text = text,
    fileName = fileName,
    filePath = filePath,
    duration = duration,
    date = date,
    isPlayList = isPlayList
)

fun AffirmationEntity.toModel() = Affirmation(
    id = id,
    text = text,
    fileName = fileName,
    filePath = filePath,
    duration = duration,
    date = date,
    isPlayList = isPlayList,
    isSelected = false
)

fun AffirmationData.asEntity() = AffirmationEntity(
    id = id,
    text = text,
    fileName = "",
    filePath = "",
    date = 0L,
    duration = "",
    isPlayList = false
)
