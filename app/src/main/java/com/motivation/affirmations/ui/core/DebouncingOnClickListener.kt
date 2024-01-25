package com.motivation.affirmations.ui.core

import android.view.View

/**
 * A [click listener][View.OnClickListener] that debounces multiple clicks posted in the
 * same frame. A click on one button disables all buttons for that frame.
 * JakeWharton/butterknife
 */
abstract class DebouncingOnClickListener : View.OnClickListener {
    override fun onClick(v: View) {
        if (enabled) {
            enabled = false
            v.postDelayed(ENABLE_AGAIN, 500)
            doClick(v)
        }
    }

    abstract fun doClick(v: View?)

    companion object {
        var enabled = true
        private val ENABLE_AGAIN =
            Runnable { enabled = true }
    }
}
