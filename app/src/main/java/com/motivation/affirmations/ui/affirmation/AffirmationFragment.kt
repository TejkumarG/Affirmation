package com.motivation.affirmations.ui.affirmation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.motivation.affirmations.ui.ViewBindingFragment
import com.motivation.app.databinding.FragmentAffirmationBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 11.01.2024.
 */
@AndroidEntryPoint
class AffirmationFragment : ViewBindingFragment<FragmentAffirmationBinding>() {

    private val viewModel by viewModels<AffirmationViewModel>()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAffirmationBinding.inflate(inflater, container, false)
}
