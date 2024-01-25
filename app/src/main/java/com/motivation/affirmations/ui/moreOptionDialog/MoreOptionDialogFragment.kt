package com.motivation.affirmations.ui.moreOptionDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.motivation.affirmations.ui.core.BaseDialogFragment
import com.motivation.affirmations.ui.core.extensions.mainNavController
import com.motivation.app.R
import com.motivation.app.databinding.MoreOptionLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoreOptionDialogFragment : BaseDialogFragment<MoreOptionLayoutBinding>() {

    private val viewModel by viewModels<MoreOptionViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ) = MoreOptionLayoutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("affirmationId")?.let {
            viewModel.getAffirmation(it)
        }
        setListener()
        setObserver()
    }

    private fun setListener() {
        binding.apply {
            closeBtn.setOnClickListener { mainNavController.popBackStack() }
            addOrEditBtn.setOnClickListener {
                mainNavController.navigate(
                    R.id.action_moreDialog_to_nav_voice_recorder,
                    bundleOf("affirmationId" to viewModel.affirmation.value.id)
                )
            }
            deleteBtn.setOnClickListener {
                viewModel.deleteRecording(mainNavController::popBackStack)
            }
            shareBtn.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.share(viewModel.affirmation.first())
                }
            }
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            launch {
                viewModel.affirmation.collectLatest {
                    if (it.fileName.isNotEmpty()) {
                        binding.addOrEditBtn.text = getString(R.string.edit_record)
                        binding.deleteBtn.visibility = View.VISIBLE
                    } else {
                        binding.addOrEditBtn.text = getString(R.string.add_recording)
                        binding.deleteBtn.visibility = View.GONE
                    }
                }
            }
        }
    }
}