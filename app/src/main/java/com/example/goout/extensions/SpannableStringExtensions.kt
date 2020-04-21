package com.example.goout.extensions

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View

fun SpannableString.applySpannableClick(
    indexOfStart: Int,
    indexOfEnd: Int,
    callbackToOpen: () -> Unit
): SpannableString {
    return apply {
        setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    callbackToOpen.invoke()
                }
            },
            indexOfStart,
            indexOfEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

fun SpannableString.applySpannableColor(
    context: Context,
    indexOfStart: Int,
    indexOfEnd: Int,
    @ColorRes colorId: Int
): SpannableString {
    return apply {
        setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, colorId)),
            indexOfStart,
            indexOfEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

fun SpannableString.applySpannableBold(
    indexOfStart: Int,
    indexOfEnd: Int
): SpannableString {
    return apply {
        setSpan(
            StyleSpan(Typeface.BOLD),
            indexOfStart,
            indexOfEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

