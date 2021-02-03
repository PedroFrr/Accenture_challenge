package com.example.accenturechallenge.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.accenturechallenge.R

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
fun ImageView.loadImage(imageUrl: String, placeHolderResourceId: Int) {
    Glide.with(this)
        .load(imageUrl)
        .fitCenter()
        .placeholder(placeHolderResourceId)
        .error(R.drawable.ic_baseline_error_24)
        .into(this)
}