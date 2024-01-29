package com.motivation.affirmations.domain.usecases.affirmation

import com.motivation.affirmations.data.repository.AffirmationRepository
import com.motivation.affirmations.domain.model.UserAffirmation
import com.motivation.affirmations.domain.model.params.AffirmationListParam
import com.motivation.affirmations.domain.usecases.core.flow.FlowUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateRecordUseCase @Inject constructor(
    private val repo: AffirmationRepository,
    private val convertUseCase: ConvertUserAffirmationUseCase
) : FlowUseCase<UserAffirmation, AffirmationListParam>() {
    override fun run(params: AffirmationListParam): Flow<UserAffirmation> = flow {
        val affirmations = repo.updateRecords(params.list)
        emit(convertUseCase(AffirmationListParam(affirmations)))
    }
}
