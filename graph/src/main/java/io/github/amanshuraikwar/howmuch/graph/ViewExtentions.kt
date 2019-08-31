package io.github.amanshuraikwar.howmuch.graph

import android.view.View

fun View.scaledDensity() = resources.displayMetrics.scaledDensity

fun View.density() = resources.displayMetrics.density

fun View.dpToPx(dp: Float) = density() * dp

fun View.spToPx(sp: Float) = scaledDensity() * sp