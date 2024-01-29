package com.motivation.affirmations.domain.model.params

import com.motivation.affirmations.data.source.local.db.entities.AffirmationEntity
import com.motivation.affirmations.domain.usecases.core.UseCase

data class AffirmationListParam(var list: List<AffirmationEntity>) : UseCase.Params
