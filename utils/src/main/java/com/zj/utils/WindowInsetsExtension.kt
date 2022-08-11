package com.zj.utils

import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updateMarginsRelative
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative

fun View.recordInitialPaddingForView() = Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)

fun View.recordInitialMarginForView() =
    (layoutParams as? ViewGroup.MarginLayoutParams)?.let { Rect(marginLeft, marginTop, marginRight, marginBottom) }
        ?: Rect(0, 0, 0, 0)

/**
 * It is best to always call this method when setting OnApplyWindowInsetsListener,
 * otherwise OnApplyWindowInsetsListener will not be called sometimes, such as when
 * we set OnApplyWindowInsetsListener in the constructor of a view and this view will
 * be added to the ViewGroup after a delay.
 */
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.doOnApplyWindowInsets(block: (View, WindowInsetsCompat, Rect, Rect) -> Unit) {
    val initialPadding = recordInitialPaddingForView()
    val initialMargin = recordInitialMarginForView()
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding, initialMargin)
        insets
    }
    requestApplyInsetsWhenAttached()
}

fun View.applyBottomWindowInsetsForScrollingView(scrollingView: ViewGroup) {
    scrollingView.clipToPadding = false
    val scrollingViewInitialPadding = recordInitialPaddingForView()
    this.doOnApplyWindowInsets { _, insets, _, _ ->
        scrollingView.updatePadding(bottom = scrollingViewInitialPadding.bottom + insets.systemWindowInsetBottom)
    }
}

fun View.applyTopWindowInsetsForScrollingView(scrollingView: ViewGroup) {
    scrollingView.clipToPadding = false
    val scrollingViewInitialPadding = recordInitialPaddingForView()
    this.doOnApplyWindowInsets { _, insets, _, _ ->
        scrollingView.updatePadding(top = scrollingViewInitialPadding.top + insets.systemWindowInsetTop)
    }
}

fun View.applyHorizontalInsetPadding() =
    doOnApplyWindowInsets { view, insets, padding, _ ->
        view.updatePaddingRelative(
            top = padding.top + insets.systemWindowInsetTop,
            bottom = padding.bottom + insets.systemWindowInsetBottom
        )
    }

fun View.applyTopInsetPadding() =
    doOnApplyWindowInsets { view, insets, padding, _ ->
        view.updatePaddingRelative(
            top = padding.top + insets.systemWindowInsetTop
        )
    }

fun View.applyBottomInsetPadding() =
    doOnApplyWindowInsets { view, insets, padding, _ ->
        view.updatePaddingRelative(
            bottom = padding.bottom + insets.systemWindowInsetBottom
        )
    }

fun View.applyTopInsetMargin() =
    doOnApplyWindowInsets { view, insets, _, margin ->
        (view.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            updateMarginsRelative(
                top = margin.top + insets.systemWindowInsetTop
            )
        }
    }

fun View.applyBottomInsetMargin() =
    doOnApplyWindowInsets { view, insets, _, margin ->
        (view.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            updateMarginsRelative(
                bottom = margin.bottom + insets.systemWindowInsetBottom
            )
        }
    }

fun ComponentActivity.applyImmersionWithWindowInsets() {
    window.statusBarColor = Color.TRANSPARENT
    ViewCompat.getWindowInsetsController(findViewById(android.R.id.content))?.apply {
        // change statusBar text to black color.
        isAppearanceLightStatusBars = true
    }
    EdgeInsetDelegate(this).start()
}
