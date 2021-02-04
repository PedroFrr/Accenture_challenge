package com.example.accenturechallenge.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.accenturechallenge.R
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

fun Fragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    view?.let { activity?.toast(message, length) }
}

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}


/**
 * Helper functions for the View layer of the app.
 */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Glide helper functions
 */
fun ImageView.loadImage(imageUrl: String) {
    val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
        .setDuration(1800) // how long the shimmering animation takes to do one full sweep
        .setBaseAlpha(0.7f) //the alpha of the underlying children
        .setHighlightAlpha(0.6f) // the shimmer alpha amount
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(true)
        .build()

    //Placeholder for the Glide Image
    val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }

    Glide.with(this)
        .load(imageUrl)
        .fitCenter()
        .placeholder(shimmerDrawable)
        .error(R.drawable.ic_baseline_error_24)
        .into(this)
}
