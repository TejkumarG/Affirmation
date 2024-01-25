package com.motivation.affirmations.domain.usecases.affirmation

import com.motivation.affirmations.data.repository.AffirmationRepository
import com.motivation.affirmations.domain.model.UserAffirmation
import com.motivation.affirmations.domain.model.params.AffirmationListParam
import com.motivation.affirmations.domain.model.params.DeleteFromPlayListParam
import com.motivation.affirmations.domain.usecases.core.flow.FlowUseCase
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteFromPlaylistUseCase @Inject constructor(
    private val repo: AffirmationRepository,
    private val convertUseCase: ConvertUserAffirmationUseCase
) : FlowUseCase<UserAffirmation, DeleteFromPlayListParam>() {
    override fun run(params: DeleteFromPlayListParam) = flow {
        repo.removeFromPlaylist(params.ids)
        val affirmations = repo.getAffirmations()
        emit(convertUseCase(AffirmationListParam(affirmations)))
    }
}