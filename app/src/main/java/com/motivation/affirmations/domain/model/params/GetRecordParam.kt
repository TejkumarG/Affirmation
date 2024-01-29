package com.motivation.affirmations.domain.model.params

import com.motivation.affirmations.domain.usecases.core.UseCase

data class GetRecordParam(
    val id: Int
) : UseCase.Params
