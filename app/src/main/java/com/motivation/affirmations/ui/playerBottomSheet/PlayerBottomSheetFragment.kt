package com.motivation.affirmations.ui.playerBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.motivation.affirmations.domain.model.enums.PlayerState
import com.motivation.affirmations.ui.core.BaseBottomSheetDialogFragment
import com.motivation.affirmations.ui.core.extensions.mainNavController
import com.motivation.app.R
import com.motivation.app.databinding.PlayerBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerBottomSheetFragment : BaseBottomSheetDialogFragment<PlayerBottomSheetBinding>() {

    private val viewModel by viewModels<PlayerBottomSheetViewModel>()
    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ) = PlayerBottomSheetBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("affirmationId")?.let {
            viewModel.getAffirmation(it)
        }
    }

    override fun setListener() {
        binding.apply {
            stopBtn.setOnClickListener {
                viewModel.stopPlayer()
                mainNavController.popBackStack()
            }
            playBtn.setOnClickListener {
                viewModel.playPausePlayer()
            }
        }
    }

    override fun setObserver() {
        lifecycleScope.launch {
            launch {
                viewModel.playerState.collectLatest {
                    when (it) {
                        PlayerState.PLAY -> binding.playBtn.setImageResource(R.drawable.ic_pause)
                        PlayerState.PAUSE -> binding.playBtn.setImageResource(R.drawable.ic_play)
                        PlayerState.FINISH -> mainNavController.popBackStack()
                        PlayerState.ERROR, PlayerState.INITIAL -> Unit
                    }
                }
            }
            launch {
                viewModel.affirmation.collectLatest {
                    if (it.fileName.isNotEmpty()) {
                        binding.affirmationTxt.text = it.text
                    }
                }
            }
        }
    }
}
