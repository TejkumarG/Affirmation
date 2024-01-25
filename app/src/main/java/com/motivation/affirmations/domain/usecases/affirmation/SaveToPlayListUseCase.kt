package com.motivation.affirmations.domain.usecases.affirmation

import com.motivation.affirmations.data.repository.AffirmationRepository
import com.motivation.affirmations.domain.model.UserAffirmation
import com.motivation.affirmations.domain.model.params.AffirmationListParam
import com.motivation.affirmations.domain.model.params.SaveToPlayListParam
import com.motivation.affirmations.domain.usecases.core.flow.FlowUseCase
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveToPlayListUseCase @Inject constructor(
    private val repo: AffirmationRepository,
    private val convertUseCase: ConvertUserAffirmationUseCase
) : FlowUseCase<UserAffirmation, SaveToPlayListParam>() {
    override fun run(params: SaveToPlayListParam) = flow {
        repo.saveToPlaylist(params.ids)
        val affirmations = repo.getAffirmations()
        emit(convertUseCase(AffirmationListParam(affirmations)))
    }
}