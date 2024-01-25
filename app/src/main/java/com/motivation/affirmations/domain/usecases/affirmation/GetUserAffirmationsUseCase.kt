package com.motivation.affirmations.domain.usecases.affirmation

import com.motivation.affirmations.data.repository.AffirmationRepository
import com.motivation.affirmations.domain.model.UserAffirmation
import com.motivation.affirmations.domain.model.params.AffirmationListParam
import com.motivation.affirmations.domain.usecases.core.flow.FlowNoneParamsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserAffirmationsUseCase @Inject constructor(
    private val repo: AffirmationRepository,
    private val convertUseCase: ConvertUserAffirmationUseCase
) : FlowNoneParamsUseCase<UserAffirmation>(){
    override fun run(): Flow<UserAffirmation>  = flow {
        val affirmations = repo.getAffirmations()
        emit(convertUseCase(AffirmationListParam(affirmations)))
    }
}