package com.motivation.affirmations.ui.core.extensions

import android.view.View

fun Boolean.computeVisibility() =
    if (this) View.VISIBLE else View.INVISIBLE

fun Boolean.getVisibleGone() =
    if (this) View.VISIBLE else View.GONE