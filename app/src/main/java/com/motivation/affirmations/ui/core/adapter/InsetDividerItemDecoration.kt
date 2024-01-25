package com.motivation.affirmations.ui.core.adapter

import android.content.Context
import android.graphics.drawable.InsetDrawable
import androidx.recyclerview.widget.DividerItemDecoration

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 25.07.2022.
 */
class InsetDividerItemDecoration(
    private val context: Context,
    orientation: Int
) : DividerItemDecoration(context, orientation) {

    fun setInsets(insetLeft: Int, insetTop: Int, insetRight: Int, insetBottom: Int) {
        val attrs = intArrayOf(android.R.attr.listDivider)
        val typedArray = context.obtainStyledAttributes(attrs)
        val divider = typedArray.getDrawable(0)
        val insetDivider = InsetDrawable(divider, insetLeft, insetTop, insetRight, insetBottom)

        typedArray.recycle()
        setDrawable(insetDivider)
    }
}
