package kz.example.myapp.presentation.imageloader

import android.content.Context
import android.widget.ImageView

interface ImageLoader {

    fun loadPosterImg(
        context: Context,
        url: String,
        target: ImageView
    )
}