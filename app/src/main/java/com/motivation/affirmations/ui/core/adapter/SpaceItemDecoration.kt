package com.motivation.affirmations.ui.core.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 15.03.2022.
 */
open class SpaceItemDecoration @JvmOverloads constructor(
    private val top: Int,
    private val left: Int = top,
    private val right: Int = top,
    private val bottom: Int = top,
    private val addSpaceAboveFirstItem: Boolean = true,
    private val addSpaceBelowLastItem: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (addSpaceAboveFirstItem && parent.getChildLayoutPosition(view) < 1 ||
            parent.getChildLayoutPosition(view) >= 1
        ) {
            if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
                outRect.top = top
                outRect.left = left
                outRect.right = right
            } else {
                outRect.left = left
                outRect.top = top
                outRect.right = right
                outRect.bottom = bottom
            }
        }

        val childAdapterPosition = parent.getChildAdapterPosition(view)
        if (addSpaceBelowLastItem && childAdapterPosition == getTotalItemCount(parent) - 1) {
            if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
                outRect.bottom = bottom
                outRect.right = right
            } else {
                outRect.left = left
                outRect.top = top
                outRect.right = right
                outRect.bottom = bottom
            }
        }
    }

    private fun getTotalItemCount(parent: RecyclerView): Int {
        return parent.adapter?.itemCount ?: 0
    }

    private fun getOrientation(parent: RecyclerView): Int {
        return when (parent.layoutManager) {
            is LinearLayoutManager -> {
                (parent.layoutManager as LinearLayoutManager).orientation
            }
            else -> {
                throw IllegalStateException(
                    "SpaceItemDecoration can only be used with a LinearLayoutManager."
                )
            }
        }
    }
}
