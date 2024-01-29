package com.motivation.affirmations.domain.model.params

import com.motivation.affirmations.domain.usecases.core.UseCase

data class SaveToPlayListParam(val ids: List<Int>) : UseCase.Params
