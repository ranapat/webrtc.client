package org.ranapat.webrtc.example.ui.util

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.ColorInt
import java.lang.ref.WeakReference

/**
 * Helps to set clickable part in text.
 *
 * Don't forget to set android:textColorLink="@color/link" (click selector) and
 * android:textColorHighlight="@color/window_background" (background color while clicks)
 * in the TextView where you will use this.
 */
fun SpannableString.click(clickablePart: String, onClickListener: () -> Unit): SpannableString {
    val onClickWeakListener = WeakReference(onClickListener)

    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View?) {
            widget?.cancelPendingInputEvents()
            widget?.cancelLongPress()

            onClickWeakListener.get()?.invoke()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    val clickablePartStart = indexOf(clickablePart)
    setSpan(clickableSpan,
            clickablePartStart,
            clickablePartStart + clickablePart.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

/**
 * Changes color for the specified string
 */
fun SpannableString.color(@ColorInt color: Int, colorPart: String): SpannableString {
    val foregroundColorSpan = ForegroundColorSpan(color)
    val colorPartStart = indexOf(colorPart)
    setSpan(foregroundColorSpan,
            colorPartStart,
            colorPartStart + colorPart.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}