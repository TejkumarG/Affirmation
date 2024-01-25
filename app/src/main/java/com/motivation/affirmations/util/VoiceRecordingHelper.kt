package com.motivation.affirmations.util

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.motivation.affirmations.ui.core.extensions.getPlaylistPath
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class VoiceRecordingHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private var recorder: MediaRecorder? = null

    lateinit var filePath: String

    private var startTime = 0L
    private var endTime = 0L

    fun getStartTime(): Long{
        return this.startTime
    }

    fun retrieveDuration() = try {
        (endTime - startTime) / 1000
    } catch (_: Exception) {
        0
    }.toString()

    fun startRecording(fileName: String) {
        initMediaRecorder(fileName)
        startTime = System.currentTimeMillis()
        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            File(context.getPlaylistPath() ?: "").mkdir()
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(filePath)
            try {
                prepare()
            } catch (_: Exception) {}
            start()
        }
    }

    private fun initMediaRecorder(fileName: String) {
        filePath = "${context.getPlaylistPath()}/$fileName.mp3"
        startTime = 0L
        endTime = 0L
        recorder = recorder ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
    }

    fun stopRecording() {
        recorder?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pause()
            }
            stop()
            reset()
            release()
            recorder = null
        }
    }
}