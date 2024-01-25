package com.motivation.affirmations.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 14.03.2022.
 */
abstract class ViewBindingFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null

    protected val binding: VB
        get() = requireNotNull(_binding)

    protected val isBindingNull: Boolean
        get() = _binding == null

    protected val isBindingNotNull: Boolean
        get() = !isBindingNull

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        addObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyViewBinding()
    }

    protected open fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return _binding?.root
    }

    protected abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB?

    protected open fun setListeners() {
    }

    protected open fun addObservers() {
    }

    @CallSuper
    protected open fun destroyViewBinding() {
        _binding = null
    }
}
