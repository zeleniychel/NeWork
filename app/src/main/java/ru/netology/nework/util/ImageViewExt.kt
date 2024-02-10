package ru.netology.nework.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.netology.nework.R

fun ImageView.load(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_account_circle_24)
        .circleCrop()
        .into(this)
}

fun ImageView.loadAttachment(url: String) {
    Glide.with(this)
        .load(url)
        .timeout(30_0000)
        .into(this)
}