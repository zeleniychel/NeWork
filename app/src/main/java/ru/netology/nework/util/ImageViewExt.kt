package ru.netology.nework.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.load(url: String) {
    Glide.with(this)
        .load(url)
        .timeout(30_0000)
        .circleCrop()
        .into(this)
}

fun ImageView.loadAttachment(url: String) {
    Glide.with(this)
        .load(url)
        .timeout(30_0000)
        .into(this)
}