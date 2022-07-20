package kz.example.myapp.presentation.view

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kz.example.myapp.R
import kz.example.myapp.authorization.NAME_SHARED_PREFERENCES
import kz.example.myapp.content.MovieDetailActivity
import kz.example.myapp.content.data.models.MovieApiData
import kz.example.myapp.presentation.imageloader.DefaultImageLoader
import kz.example.myapp.presentation.imageloader.ImageLoader

class MovieCardViewHolder(
    itemView: View,
    onMovieListener: OnMovieListener,
    private val posterImgLoader: ImageLoader = DefaultImageLoader()
) : RecyclerView.ViewHolder(itemView) {

    private val posterMovieImageView: ImageView = itemView.findViewById(R.id.category_item_image_view_movie_poster)
    private val titleMovieTextView: TextView = itemView.findViewById(R.id.category_item_text_view_movie_title)
    private val onMovieListener: OnMovieListener = onMovieListener

    fun onBind(movieApiData: MovieApiData){
        titleMovieTextView.text = movieApiData.title
        posterImgLoader.loadPosterImg(
            context = posterMovieImageView.context,
            url = movieApiData.poster_path.orEmpty(),
            target = posterMovieImageView
        )

        itemView.setOnClickListener {
            movieApiData.id?.let { it1 -> onMovieListener.onMovieClick(it1) }
        }
    }
}