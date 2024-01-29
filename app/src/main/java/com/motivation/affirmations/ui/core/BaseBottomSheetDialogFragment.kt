package com.motivation.affirmations.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 20.05.2022.
 */

abstract class BaseBottomSheetDialogFragment<T : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: T? = null

    val binding: T
        get() = checkNotNull(_binding)

    val isBindingNotNull
        get() = _binding != null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = createViewBinding(inflater, container, false)
        return binding.root
    }

    abstract fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(dialog?.window ?: return) {
            isCancelable = false
            setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        setListener()
        setObserver()
    }

    protected open fun setListener() {}

    protected open fun setObserver() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
