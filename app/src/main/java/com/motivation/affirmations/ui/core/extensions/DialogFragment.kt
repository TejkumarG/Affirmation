package com.motivation.affirmations.ui.core.extensions

import android.view.KeyEvent
import androidx.fragment.app.DialogFragment

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 20.05.2022.
 */
inline fun DialogFragment.addOnBackPressedCallback(
    crossinline onBackPressed: () -> Unit
) {
    dialog?.setOnKeyListener { _, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            onBackPressed()
            true
        } else {
            false
        }
    }
}
