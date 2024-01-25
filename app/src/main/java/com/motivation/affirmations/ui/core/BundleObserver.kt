package com.motivation.affirmations.ui.core

import android.os.Bundle
import androidx.lifecycle.Observer

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 14.10.2022.
 *
 * An [Observer] for [Bundle]s, simplifying the pattern of checking if the [Bundle]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Bundle]'s contents has not been handled.
 */
class BundleObserver(private val onEventUnhandledContent: (Bundle) -> Unit) : Observer<Bundle> {
    override fun onChanged(bundle: Bundle) {
        if (bundle.isHandled) return

        bundle.isHandled = true
        onEventUnhandledContent(bundle)
    }
}

private var Bundle.isHandled
    get() = getBoolean("handled")
    set(value) {
        putBoolean("handled", value)
    }
