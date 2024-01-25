package com.motivation.affirmations.domain.usecases.affirmation

import com.motivation.affirmations.data.repository.AffirmationRepository
import com.motivation.affirmations.data.source.mapper.toModel
import com.motivation.affirmations.domain.model.Affirmation
import com.motivation.affirmations.domain.model.params.GetRecordParam
import com.motivation.affirmations.domain.usecases.core.flow.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecordByIdUseCase @Inject constructor(
    private val repo: AffirmationRepository
) : FlowUseCase<Affirmation, GetRecordParam>() {
    override fun run(params: GetRecordParam): Flow<Affirmation> = flow {
        emit(repo.getRecord(params.id).toModel())
    }
}