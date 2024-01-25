package com.motivation.affirmations.ui.voice_recorder

import androidx.lifecycle.viewModelScope
import com.motivation.affirmations.data.source.mapper.toEntity
import com.motivation.affirmations.domain.model.Affirmation
import com.motivation.affirmations.domain.model.enums.ActionState
import com.motivation.affirmations.domain.model.enums.PlayerState
import com.motivation.affirmations.domain.model.enums.RecordingState
import com.motivation.affirmations.domain.model.params.GetRecordParam
import com.motivation.affirmations.domain.model.params.SaveRecordParam
import com.motivation.affirmations.domain.usecases.affirmation.GetRecordByIdUseCase
import com.motivation.affirmations.domain.usecases.affirmation.SaveRecordUseCase
import com.motivation.affirmations.domain.usecases.affirmation.DeleteRecordUseCase
import com.motivation.affirmations.ui.core.BaseViewModel
import com.motivation.affirmations.util.MediaPlayerHelper
import com.motivation.affirmations.util.VoiceRecordingHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoiceRecorderViewModel @Inject constructor(
    private val voiceRecorder: VoiceRecordingHelper,
    private val mediaPlayer: MediaPlayerHelper,
    private val getRecord: GetRecordByIdUseCase,
    private val saveUseCase: SaveRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase
) : BaseViewModel() {

    private val _playerState = MutableStateFlow(PlayerState.INITIAL)
    val playerState = _playerState.asStateFlow()

    private val _recorderState = MutableStateFlow(RecordingState.INITIAL)
    val recorderState = _recorderState.asStateFlow()

    private val _affirmation = MutableStateFlow(Affirmation())
    val affirmation = _affirmation.asStateFlow()

    private val fileName = MutableStateFlow("")

    fun getAffirmation(affirmationId: Int) {
        viewModelScope.launch {
            getRecord.run(GetRecordParam(affirmationId)).collectLatest { affirmation ->
                _affirmation.update { affirmation }
                fileName.update { "${_affirmation.value.id}" }
            }
        }
    }

    fun startRecording() {
        if (fileName.value.isEmpty()) {
            _recorderState.update { RecordingState.ERROR }
            return
        }
        _recorderState.update { RecordingState.RECORD }
        voiceRecorder.startRecording(fileName.value)
    }

    fun pauseRecording() {
        voiceRecorder.stopRecording()
        _recorderState.update { RecordingState.PAUSE }
        try {
            mediaPlayer.stop()
            mediaPlayer.initSource(fileName.value)
        } catch (_: Exception) {
            _playerState.update { PlayerState.ERROR }
        }
    }

    fun deleteRecording() {
        if (recorderState.value == RecordingState.RECORD)
            pauseRecording()
        if (playerState.value == PlayerState.PLAY)
            mediaPlayer.stop()
        _recorderState.update { RecordingState.INITIAL }
        _playerState.update { PlayerState.INITIAL }
    }

    fun editRecording() {
        deleteRecording()
//        _recorderState.update { RecordingState.INITIAL }
//        voiceRecorder.stopRecording()
//        mediaPlayer.stop()
    }

    fun playRecording() {
        _playerState.update { PlayerState.PLAY }
        mediaPlayer.play {
            _playerState.update { PlayerState.PAUSE }
        }
    }

    fun pausePlaying() {
        _playerState.update { PlayerState.PAUSE }
        mediaPlayer.pause()
    }

    fun stopPlayer() {
        mediaPlayer.stop()
    }

    fun saveRecord(): StateFlow<ActionState> {
        val param = SaveRecordParam(
            fileName.value,
            voiceRecorder.filePath,
            voiceRecorder.getStartTime(),
            voiceRecorder.retrieveDuration(),
            affirmation.value.toEntity()
        )
        return saveUseCase(param).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            ActionState.PENDING
        )
    }
}