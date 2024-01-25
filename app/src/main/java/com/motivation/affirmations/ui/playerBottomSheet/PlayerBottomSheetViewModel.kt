package com.motivation.affirmations.ui.playerBottomSheet

import androidx.lifecycle.viewModelScope
import com.motivation.affirmations.domain.model.Affirmation
import com.motivation.affirmations.domain.model.enums.PlayerState
import com.motivation.affirmations.domain.model.params.GetRecordParam
import com.motivation.affirmations.domain.usecases.affirmation.GetRecordByIdUseCase
import com.motivation.affirmations.ui.core.BaseViewModel
import com.motivation.affirmations.util.MediaPlayerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerBottomSheetViewModel @Inject constructor(
    private val mediaPlayer: MediaPlayerHelper,
    private val getRecord: GetRecordByIdUseCase,
): BaseViewModel() {

    private val _affirmation = MutableStateFlow(Affirmation())
    val affirmation = _affirmation.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerState.INITIAL)
    val playerState = _playerState.asStateFlow()

    fun getAffirmation(affirmationId: Int) {
        viewModelScope.launch {
            getRecord.run(GetRecordParam(affirmationId)).collectLatest { affirmation ->
                _affirmation.update { affirmation }
                if(affirmation.fileName.isNotEmpty()) {
                    playPausePlayer()
                }
            }
        }
    }

    fun playPausePlayer() {
        when(playerState.value) {
            PlayerState.INITIAL -> {
                mediaPlayer.apply {
                    initSource(affirmation.value.fileName)
                    play {
                        _playerState.update { PlayerState.FINISH }
                    }
                    _playerState.update { PlayerState.PLAY }
                }

            }
            PlayerState.PLAY -> {
                mediaPlayer.pause()
                _playerState.update { PlayerState.PAUSE }
            }
            PlayerState.PAUSE -> {
                mediaPlayer.play {
                    _playerState.update { PlayerState.FINISH }
                }
                _playerState.update { PlayerState.PLAY }
            }
            PlayerState.FINISH, PlayerState.ERROR -> Unit
        }
    }

    fun stopPlayer() {
        mediaPlayer.stop()
    }
}