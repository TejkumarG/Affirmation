package com.motivation.affirmations.ui.core.extensions

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.motivation.app.databinding.SavedPopupBinding

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

fun Context.getPlaylistPath() = externalCacheDir?.absolutePath

fun Context.showSavedDialog(message: String, @DrawableRes imgId: Int? = null) = Dialog(this).apply {
    val binding = SavedPopupBinding.inflate(LayoutInflater.from(this@showSavedDialog))
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setCancelable(false)
    setContentView(binding.root)
    imgId?.let {
        binding.dialogImg.setImageResource(it)
    } ?: run {
        binding.dialogImg.visibility = View.GONE
    }
    binding.dialogTxt.text = message
    show()
}