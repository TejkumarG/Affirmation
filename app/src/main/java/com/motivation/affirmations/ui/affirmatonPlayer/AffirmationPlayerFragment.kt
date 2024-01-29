package com.motivation.affirmations.ui.affirmatonPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.motivation.affirmations.domain.model.enums.PlayerState
import com.motivation.affirmations.ui.ViewBindingFragment
import com.motivation.affirmations.ui.core.extensions.mainNavController
import com.motivation.affirmations.ui.core.extensions.showToastShort
import com.motivation.app.databinding.FragmentAffirmationPlayerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AffirmationPlayerFragment : ViewBindingFragment<FragmentAffirmationPlayerBinding>() {

    private val viewModel by viewModels<AffirmationPlayerViewModel>()
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAffirmationPlayerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPlaylist {
            showToastShort("Unable to fetch the playlist")
            mainNavController.popBackStack()
        }
    }

    override fun setListeners() {
        binding.apply {
            nextBtn.setOnClickListener {
                viewModel.playNext()
            }
            stopBtn.setOnClickListener {
                viewModel.stopPlayer()
                mainNavController.popBackStack()
            }
            backBtn.setOnClickListener {
                mainNavController.popBackStack()
            }
        }
    }

    override fun addObservers() {
        lifecycleScope.launch {
            launch {
                viewModel.playerState.collectLatest {
                    when (it) {
                        PlayerState.FINISH -> viewModel.playNext()
                        else -> Unit
                    }
                }
            }
            launch {
                viewModel.affirmation.collectLatest {
                    binding.affirmationTxt.text = it.text
                }
            }
        }
    }
}
