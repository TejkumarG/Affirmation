package com.motivation.affirmations.ui.voice_recorder

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.motivation.affirmations.domain.model.enums.ActionState
import com.motivation.affirmations.domain.model.enums.PlayerState
import com.motivation.affirmations.domain.model.enums.RecordingState
import com.motivation.affirmations.ui.ViewBindingFragment
import com.motivation.affirmations.ui.core.extensions.checkSelfPermission
import com.motivation.affirmations.ui.core.extensions.mainNavController
import com.motivation.affirmations.ui.core.extensions.showSavedDialog
import com.motivation.app.R
import com.motivation.app.databinding.DeleteRecordingPopupBinding
import com.motivation.app.databinding.FragmentVoiceRecorderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class VoiceRecorderFragment : ViewBindingFragment<FragmentVoiceRecorderBinding>() {

    private val viewModel by viewModels<VoiceRecorderViewModel>()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentVoiceRecorderBinding =
        FragmentVoiceRecorderBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("affirmationId")?.let {
            viewModel.getAffirmation(it)
        }
    }

    override fun addObservers() {
        lifecycleScope.launch {
            launch {
                viewModel.recorderState.collectLatest { recordingState ->
                    when (recordingState) {
                        RecordingState.INITIAL, RecordingState.RECORD -> binding.apply {
                            btnVisibility(false)
                            if (recordingState == RecordingState.INITIAL) {
                                playPauseImg.setImageResource(R.drawable.ic_record)
                                playPauseTxt.text = getString(R.string.tap_to_record)
                            } else {
                                playPauseImg.setImageResource(R.drawable.ic_pause)
                                playPauseTxt.text = getString(R.string.recording)
                            }
                        }

                        RecordingState.PAUSE -> binding.apply {
                            btnVisibility(true)
                            playPauseImg.setImageResource(R.drawable.ic_play)
                            playPauseTxt.text = getString(R.string.tap_to_play)
                        }

                        RecordingState.FINISH -> binding.apply {
                            mainNavController.popBackStack()
                        }

                        RecordingState.ERROR -> binding.apply {
                            btnVisibility(false)
                            playPauseImg.setImageResource(R.drawable.ic_error_mic)
                            playPauseTxt.text = getString(R.string.recording_error)
                        }
                    }
                }
            }
            launch {
                viewModel.playerState.collectLatest {
                    when (it) {
                        PlayerState.INITIAL -> Unit
                        PlayerState.PLAY -> binding.apply {
                            btnVisibility(true)
                            playPauseImg.setImageResource(R.drawable.ic_pause)
                            playPauseTxt.text = getString(R.string.playing)
                        }

                        PlayerState.PAUSE -> binding.apply {
                            btnVisibility(true)
                            playPauseImg.setImageResource(R.drawable.ic_play)
                            playPauseTxt.text = getString(R.string.tap_to_play)
                        }

                        PlayerState.ERROR -> binding.apply {
                            btnVisibility(false)
                            playPauseImg.setImageResource(R.drawable.ic_error_mic)
                            playPauseTxt.text = getString(R.string.recording_error)
                        }

                        PlayerState.FINISH -> Unit
                    }
                }
            }
            launch {
                viewModel.affirmation.collectLatest {
                    binding.displayTxt.text = it.text
                }
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            playPauseContainer.setOnClickListener {
                if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)) {
                    playPauseClick()
                } else {
                    recordPermissionsLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                }
            }
            deleteImg.setOnClickListener { showDeleteDialog() }

            editImg.setOnClickListener { viewModel.editRecording() }

            saveBtn.setOnClickListener { saveRecording() }

            backButtonImg.setOnClickListener { mainNavController.popBackStack() }
        }
    }

    private fun playPauseClick() {
        when (binding.playPauseTxt.text) {
            getString(R.string.tap_to_record), getString(R.string.recording_error) -> viewModel.startRecording()
            getString(R.string.recording) -> viewModel.pauseRecording()
            getString(R.string.tap_to_play) -> viewModel.playRecording()
            getString(R.string.playing) -> viewModel.pausePlaying()
        }
    }

    private fun saveRecording() {
        lifecycleScope.launch {
            viewModel.saveRecord().collectLatest {
                if (it == ActionState.SUCCESS) {
                    val dialog = requireContext().showSavedDialog(
                        getString(R.string.saved),
                        R.drawable.saved_smiley
                    )

                    lifecycleScope.launch(Dispatchers.IO) {
                        delay(3_000)
                        withContext(Dispatchers.Main) {
                            dialog.dismiss()
                            mainNavController.popBackStack()
                            viewModel.stopPlayer()
                        }
                    }
                }
            }
        }
    }

    private fun showDeleteDialog()  = Dialog(requireContext()).apply {
        val binding = DeleteRecordingPopupBinding.inflate(LayoutInflater.from(requireContext()))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(binding.root)
        binding.apply {
            deleteBtn.setOnClickListener {
                viewModel.deleteRecording()
                dismiss()
            }

            cancelBtn.setOnClickListener { dismiss() }

            closeBtn.setOnClickListener { dismiss() }
        }
        show()
    }

    private fun btnVisibility(canShow: Boolean) {
        val visible = if (canShow) VISIBLE else GONE
        binding.apply {
            saveBtn.visibility = visible
            editImg.visibility = visible
            deleteImg.visibility = visible
        }
    }

    private val recordPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) viewModel.startRecording()
        }
}