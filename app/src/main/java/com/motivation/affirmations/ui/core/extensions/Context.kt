package com.motivation.affirmations.ui.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat

fun Context.toast(message: CharSequence): Toast {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

internal fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

val Context.isDarkMode: Boolean
    get() {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

fun Context.drawableCompat(resId: Int) = ContextCompat.getDrawable(this, resId)

@SuppressLint("DiscouragedApi")
fun Context.drawableCompat(name: String) = try {
    val resId = resources.getIdentifier(name, "drawable", packageName)

    drawableCompat(resId)
} catch (ex: Resources.NotFoundException) {
    null
}
