package kz.example.myapp.presentation.imageloader

import android.content.Context
import android.view.RoundedCorner
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kz.example.myapp.R

private const val API_BASE_URL_FOR_IMG = "https://image.tmdb.org/t/p/w500"

class DefaultImageLoader: ImageLoader {

    override fun loadPosterImg(
        context: Context,
        url: String,
        target: ImageView
    ) {
        Glide.with(context)
            .load(API_BASE_URL_FOR_IMG + url)
            .error(R.drawable.temp_movie_poster)
            .transform(
                RoundedCorners(
                    context.resources.getDimensionPixelSize(
                        R.dimen.img_corners_radius
                    )
                )
            )
            .into(target)
    }
}