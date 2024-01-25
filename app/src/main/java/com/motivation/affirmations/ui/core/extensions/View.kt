package com.motivation.affirmations.ui.core.extensions

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.inputmethod.InputMethodManager
import androidx.core.animation.addListener
import dev.chrisbanes.insetter.applyInsetter
import kotlin.coroutines.resume
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 10.03.2022.
 */
suspend fun ValueAnimator.await() = suspendCancellableCoroutine { continuation ->
    this.addListener(
        onEnd = {
            continuation.resume(Unit)
        }
    )
}

suspend fun ViewPropertyAnimator.await() = suspendCancellableCoroutine {
    setListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animator) {
            if (it.isActive) {
                it.resume(this@await)
            }
        }

        override fun onAnimationCancel(animation: Animator) {
            if (it.isActive) {
                it.cancel()
            }
        }

        override fun onAnimationRepeat(animation: Animator) {
        }
    })
        .start()
}

@Suppress("DEPRECATION")
suspend fun View.showKeyboard(delay: Long = 300) {
    requestFocus()
    delay(delay)
    val inputMethodManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    if (Build.VERSION.SDK_INT >= 31) {
        inputMethodManager?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    } else {
        inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }
}

fun View.applyStatusBarsInsetter() = applyInsetter {
    type(statusBars = true) {
        margin()
    }
}

fun View.applyKeyboardInsetter() = applyInsetter {
    type(ime = true, navigationBars = true) {
        margin()
    }
}
