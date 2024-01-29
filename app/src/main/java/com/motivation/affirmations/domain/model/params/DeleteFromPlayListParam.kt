package com.motivation.affirmations.domain.model.params

import com.motivation.affirmations.domain.usecases.core.UseCase

data class DeleteFromPlayListParam(val ids: List<Int>) : UseCase.Params
