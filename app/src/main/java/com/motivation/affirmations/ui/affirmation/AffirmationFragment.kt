package com.motivation.affirmations.ui.affirmation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.motivation.affirmations.domain.model.enums.AffirmationClickEvent
import com.motivation.affirmations.ui.ViewBindingFragment
import com.motivation.affirmations.ui.core.extensions.computeVisibility
import com.motivation.affirmations.ui.core.extensions.getVisibleGone
import com.motivation.affirmations.ui.core.extensions.mainNavController
import com.motivation.affirmations.ui.core.extensions.showSavedDialog
import com.motivation.affirmations.ui.core.extensions.showToastShort
import com.motivation.app.R
import com.motivation.app.databinding.FragmentAffirmationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 11.01.2024.
 */
@AndroidEntryPoint
class AffirmationFragment : ViewBindingFragment<FragmentAffirmationBinding>() {

    private val viewModel by viewModels<AffirmationViewModel>()
    private val affirmationAdapter = AffirmationsListAdapter()
    private val playListAdapter = AffirmationsListAdapter()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAffirmationBinding.inflate(inflater, container, false).apply {
        affirmationRV.adapter = affirmationAdapter
        playlistRv.adapter = playListAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.reload()
    }

    override fun setListeners() {
        affirmationAdapter.onItemClick(getAffirmationEventHandler {
            binding.affirmationSelectAllLyt.visibility =
                affirmationAdapter.isSelectionIsAvailable().computeVisibility()
            binding.selectAllChk.isChecked = affirmationAdapter.isAllSelected()
        })
        playListAdapter.onItemClick(getAffirmationEventHandler {
            binding.playlistSelectAllLyt.visibility =
                playListAdapter.isSelectionIsAvailable().getVisibleGone()
            binding.playlistSelectAllChk.isChecked = playListAdapter.isAllSelected()
        })

        binding.apply {
            playlistPlayBtn.setOnClickListener {
                if (playListAdapter.itemCount != 0) {
                    mainNavController.navigate(
                        R.id.action_nav_affirmation_to_nav_player
                    )
                } else {
                    val dialog = requireContext().showSavedDialog("you have no items in playlist")

                    lifecycleScope.launch(Dispatchers.IO) {
                        delay(3_000)
                        withContext(Dispatchers.Main) {
                            dialog.dismiss()
                        }
                    }
                }
            }

            selectAllChk.setOnClickListener {
                if (selectAllChk.isChecked)
                    affirmationAdapter.selectAll()
                else
                    affirmationAdapter.unSelectAll()
            }
            playlistSelectAllChk.setOnClickListener {
                if (playlistSelectAllChk.isChecked)
                    playListAdapter.selectAll()
                else
                    playListAdapter.unSelectAll()
            }
            addToPlaylistBtn.setOnClickListener {
                viewModel.addToPlayList(affirmationAdapter.selectedIds())
            }
            removeFromPlaylistBtn.setOnClickListener {
                viewModel.removeFromPlayList(playListAdapter.selectedIds())
            }
        }
    }

    private fun getAffirmationEventHandler(onSelectChange: () -> Unit): (AffirmationClickEvent, Any) -> Unit {
        return { event, pos ->
            when (event) {
                AffirmationClickEvent.ON_SELECT -> {
                    onSelectChange()
                }

                AffirmationClickEvent.ON_RECORD -> mainNavController.navigate(
                    R.id.action_nav_affirmation_to_nav_voice_recorder,
                    bundleOf("affirmationId" to pos)
                )

                AffirmationClickEvent.ON_MORE -> mainNavController.navigate(
                    R.id.action_nav_affirmation_to_moreDialog,
                    bundleOf("affirmationId" to pos)
                )

                AffirmationClickEvent.ON_PLAY -> mainNavController.navigate(
                    R.id.action_nav_affirmation_to_playerDialog,
                    bundleOf("affirmationId" to pos)
                )
            }
        }
    }

    override fun addObservers() {
        lifecycleScope.launch {
            viewModel.affirmations.collectLatest {
                binding.apply {
                    playlistRv.visibility = it.playlist.isNotEmpty().getVisibleGone()
                    divider.visibility = it.playlist.isNotEmpty().getVisibleGone()
                    displayTextTxt.visibility = it.playlist.isEmpty().getVisibleGone()
                    playlistSelectAllLyt.visibility = View.GONE
                    affirmationSelectAllLyt.visibility = View.INVISIBLE
                }
                playListAdapter.submitList(it.playlist)
                affirmationAdapter.submitList(it.nonPlayList)
            }
        }
    }
}
