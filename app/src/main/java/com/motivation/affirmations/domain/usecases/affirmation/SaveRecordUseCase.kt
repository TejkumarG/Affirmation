package com.motivation.affirmations.domain.usecases.affirmation

import com.motivation.affirmations.data.repository.AffirmationRepository
import com.motivation.affirmations.domain.model.enums.ActionState
import com.motivation.affirmations.domain.model.params.SaveRecordParam
import com.motivation.affirmations.domain.usecases.core.flow.FlowUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class SaveRecordUseCase @Inject constructor(
    private val repo: AffirmationRepository
) : FlowUseCase<ActionState, SaveRecordParam>() {
    override fun run(params: SaveRecordParam) = flow {
        emit(ActionState.PROGRESS)
        repo.saveRecord(params.updated())
        emit(ActionState.SUCCESS)
    }
}
