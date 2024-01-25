package com.motivation.affirmations.util

import android.content.Context
import android.media.MediaPlayer
import com.motivation.affirmations.ui.core.extensions.getPlaylistPath
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class MediaPlayerHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private var player: MediaPlayer? = null

    @Throws(IOException::class)
    fun initSource(fileName: String) {
        initPlayer()
        player?.apply {
            setDataSource("${context.getPlaylistPath()}/$fileName.mp3")
            prepare()
        }
    }

    private fun initPlayer() {
        player = player ?: MediaPlayer()
    }

    fun play(onFinish: () -> Unit) {
        player?.start()
        onFinish(onFinish)
    }

    fun pause() {
        player?.pause()
    }

    fun stop() {
        player?.apply {
            stop()
            release()
            player = null
        }
    }

    private fun onFinish(finish: () -> Unit) {
        player?.setOnCompletionListener {
            finish()
        }
    }
}