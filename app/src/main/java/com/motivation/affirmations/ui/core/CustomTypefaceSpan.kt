package com.motivation.affirmations.ui.core

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

/**
 * Created by Vladislav Polyakov email(polyakov.vladislav94@gmail.com) on 30.11.2022.
 */
class CustomTypefaceSpan(family: String?, type: Typeface) : TypefaceSpan(family) {
    private val newType: Typeface

    init {
        newType = type
    }

    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newType)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType)
    }

    companion object {
        internal fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
            val oldStyle: Int
            val old: Typeface = paint.getTypeface()
            oldStyle = old.style
            val fake = oldStyle and tf.style.inv()
            if (fake and Typeface.BOLD != 0) {
                paint.setFakeBoldText(true)
            }
            if (fake and Typeface.ITALIC != 0) {
                paint.setTextSkewX(-0.25f)
            }
            paint.setTypeface(tf)
        }
    }
}
