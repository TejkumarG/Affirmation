package com.motivation.affirmations.domain.usecases.affirmation

import com.motivation.affirmations.data.repository.AffirmationRepository
import com.motivation.affirmations.domain.model.enums.ActionState
import com.motivation.affirmations.domain.model.params.DeleteRecordParam
import com.motivation.affirmations.domain.usecases.core.flow.FlowUseCase
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    private val repo: AffirmationRepository
) : FlowUseCase<ActionState, DeleteRecordParam>() {
    override fun run(params: DeleteRecordParam) = flow {
        emit(ActionState.PROGRESS)
        repo.saveRecord(params.deleted())
        emit(ActionState.SUCCESS)
    }
}