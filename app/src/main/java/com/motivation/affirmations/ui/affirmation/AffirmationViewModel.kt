package com.motivation.affirmations.ui.affirmation

import com.motivation.affirmations.domain.model.UserAffirmation
import com.motivation.affirmations.domain.model.params.AffirmationListParam
import com.motivation.affirmations.domain.model.params.DeleteFromPlayListParam
import com.motivation.affirmations.domain.model.params.LoadUserAffirmationsParam
import com.motivation.affirmations.domain.model.params.SaveToPlayListParam
import com.motivation.affirmations.domain.usecases.affirmation.DeleteFromPlaylistUseCase
import com.motivation.affirmations.domain.usecases.affirmation.GetUserAffirmationsUseCase
import com.motivation.affirmations.domain.usecases.affirmation.SaveToPlayListUseCase
import com.motivation.affirmations.domain.usecases.affirmation.UpdateRecordUseCase
import com.motivation.affirmations.domain.usecases.core.UseCase
import com.motivation.affirmations.ui.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 11.01.2024.
 */
@HiltViewModel
class AffirmationViewModel @Inject constructor(
    private val getAffirmationsUseCase: GetUserAffirmationsUseCase,
    private val updateRecordUseCase: UpdateRecordUseCase,
    private val saveToPlayListUseCase: SaveToPlayListUseCase,
    private val deleteFromPlaylistUseCase: DeleteFromPlaylistUseCase,
) : BaseViewModel() {

    private val _actions : MutableStateFlow<UseCase.Params> = MutableStateFlow(UseCase.None)
    @OptIn(ExperimentalCoroutinesApi::class)
    val affirmations = _actions.flatMapLatest {
        when (it) {
            is LoadUserAffirmationsParam -> getAffirmationsUseCase()
            is AffirmationListParam -> updateRecordUseCase(it)
            is SaveToPlayListParam -> saveToPlayListUseCase(it)
            is DeleteFromPlayListParam -> deleteFromPlaylistUseCase(it)
            else -> {
                flow {
                    emit(UserAffirmation())
                }
            }
        }
    }

    fun reload(){
        _actions.value = LoadUserAffirmationsParam
    }

    fun addToPlayList(ids : List<Int>){
        _actions.value = SaveToPlayListParam(ids)
    }

    fun removeFromPlayList(ids : List<Int>){
        _actions.value = DeleteFromPlayListParam(ids)
    }
}