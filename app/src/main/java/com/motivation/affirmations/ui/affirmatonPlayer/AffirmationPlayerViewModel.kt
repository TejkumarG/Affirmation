package com.motivation.affirmations.ui.affirmatonPlayer

import androidx.lifecycle.viewModelScope
import com.motivation.affirmations.domain.model.Affirmation
import com.motivation.affirmations.domain.model.enums.PlayerState
import com.motivation.affirmations.domain.usecases.affirmation.GetUserAffirmationsUseCase
import com.motivation.affirmations.ui.core.BaseViewModel
import com.motivation.affirmations.util.MediaPlayerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AffirmationPlayerViewModel @Inject constructor(
    private val mediaPlayer: MediaPlayerHelper,
    private val getAffirmations: GetUserAffirmationsUseCase
) : BaseViewModel() {

    private val _playList = MutableStateFlow(emptyList<Affirmation>())

    private val _affirmation = MutableStateFlow(Affirmation())
    val affirmation = _affirmation.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerState.INITIAL)
    val playerState = _playerState.asStateFlow()

    private var job: Job? = null
    fun getPlaylist(finish: () -> Unit) {
        viewModelScope.launch {
            getAffirmations.run().collectLatest { affirmation ->
                _playList.update { affirmation.playlist }
                if (_playList.value.isEmpty()) {
                    finish()
                    return@collectLatest
                }
                _affirmation.update { _playList.value[0] }
                playPausePlayer()
            }
        }
    }

    fun playPausePlayer() {
        when (playerState.value) {
            PlayerState.INITIAL -> {
                if (affirmation.value.fileName.isEmpty()) {
                    playNoAudioAffirmation()
                } else {
                    try {
                        mediaPlayer.apply {
                            initSource(affirmation.value.fileName)
                            play {
                                stop()
                                _playerState.update { PlayerState.FINISH }
                            }
                            _playerState.update { PlayerState.PLAY }
                        }
                    } catch (_: Exception) {
                        playNoAudioAffirmation()
                    }
                }
            }
            PlayerState.PLAY, PlayerState.PAUSE, PlayerState.FINISH, PlayerState.ERROR -> Unit
        }
    }

    private fun playNoAudioAffirmation() {
        job = viewModelScope.launch(Dispatchers.IO) {
            delay(5_000L)
            playNext()
        }
    }

    fun stopPlayer() {
        mediaPlayer.stop()
    }

    fun playNext() {
        mediaPlayer.stop()
        job?.cancel()
        job = null
        val currentIndex = _playList.value.indexOfFirst { it.id == affirmation.value.id }
        val nextAffirmation = (currentIndex + 1) % _playList.value.size
        _affirmation.update { _playList.value[nextAffirmation] }
        _playerState.update { PlayerState.INITIAL }
        playPausePlayer()
    }
}
