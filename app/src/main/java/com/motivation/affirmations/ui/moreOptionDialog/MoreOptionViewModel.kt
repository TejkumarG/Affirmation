package com.motivation.affirmations.ui.moreOptionDialog

import androidx.lifecycle.viewModelScope
import com.motivation.affirmations.data.source.mapper.toEntity
import com.motivation.affirmations.domain.model.Affirmation
import com.motivation.affirmations.domain.model.params.DeleteRecordParam
import com.motivation.affirmations.domain.model.params.GetRecordParam
import com.motivation.affirmations.domain.usecases.affirmation.DeleteRecordUseCase
import com.motivation.affirmations.domain.usecases.affirmation.GetRecordByIdUseCase
import com.motivation.affirmations.ui.core.BaseViewModel
import com.motivation.affirmations.util.ContentShareHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreOptionViewModel @Inject constructor(
    private val getRecord: GetRecordByIdUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val shareHelper: ContentShareHelper
) : BaseViewModel() {

    private val _affirmation = MutableStateFlow(Affirmation())
    val affirmation = _affirmation.asStateFlow()

    fun getAffirmation(affirmationId: Int) {
        viewModelScope.launch {
            getRecord.run(GetRecordParam(affirmationId)).collectLatest { affirmation ->
                _affirmation.update { affirmation }
            }
        }
    }

    fun deleteRecording(onFinish: () -> Unit) {
        viewModelScope.launch {
            deleteRecordUseCase(DeleteRecordParam(affirmation.value.toEntity()))
            onFinish()
        }
    }

    fun share(affirmation: Affirmation){
        shareHelper.share(affirmation)
    }
}