package com.motivation.affirmations.domain.usecases.affirmation

import com.motivation.affirmations.data.source.local.db.entities.AffirmationEntity
import com.motivation.affirmations.data.source.mapper.toModel
import com.motivation.affirmations.domain.model.UserAffirmation
import com.motivation.affirmations.domain.model.params.AffirmationListParam
import com.motivation.affirmations.domain.usecases.core.SimpleUseCase
import javax.inject.Inject

class ConvertUserAffirmationUseCase @Inject constructor() :
    SimpleUseCase<UserAffirmation, AffirmationListParam>() {
    override fun run(params: AffirmationListParam): UserAffirmation {
        val affirmations = params.list.map(AffirmationEntity::toModel)
        val (playlist, nonPlaylist) = affirmations.partition { it.isPlayList }
        return UserAffirmation(affirmations, playlist, nonPlaylist)
    }
}