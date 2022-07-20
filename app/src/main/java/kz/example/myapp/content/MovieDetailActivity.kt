package kz.example.myapp.content

import android.content.Context
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import kz.example.myapp.R
import kz.example.myapp.authorization.NAME_SHARED_PREFERENCES
import kz.example.myapp.content.data.models.MovieDetailApiData
import kz.example.myapp.network.apiClient
import kz.example.myapp.network.ID_KEY
import kz.example.myapp.network.TAG
import kz.example.myapp.presentation.imageloader.DefaultImageLoader
import kz.example.myapp.presentation.imageloader.ImageLoader
import okhttp3.ResponseBody
import org.json.JSONArray

class MovieDetailActivity : AppCompatActivity() {
//    private val movieId = getSavedMovieDetailId()

    private lateinit var backdropPathImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var taglineTextView: TextView
    private lateinit var overviewTextView: TextView
    //    private lateinit var genresRecyclerView: RecyclerView

    private lateinit var movieDetailData: MovieDetailApiData
    private val posterImgLoader: ImageLoader = DefaultImageLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        onBind()


        loadApiData()
    }

    private fun onBind() {
        backdropPathImageView = findViewById(R.id.movie_detail_activity_image_view_movie_backdrop_path)
        titleTextView = findViewById(R.id.movie_detail_activity_text_view_movie_title)
        genreTextView = findViewById(R.id.movie_detail_activity_text_view_movie_genre)
        taglineTextView = findViewById(R.id.movie_detail_activity_text_view_movie_tagline)
        overviewTextView = findViewById(R.id.movie_detail_activity_text_view_movie_overview)
    }

    private fun loadApiData() {
        val movieId = getSavedMovieDetailId()

        apiClient.getMovieById(movieId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body()!!
                val responseJson = JSONObject(responseBody.string())

                movieDetailData = parseMovieDetailJsonObject(responseJson)

                displayToScreenMovieDetail()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("TAG", t.localizedMessage ?: "Failure Occurred")
            }
        })
    }

    private fun parseMovieDetailJsonObject(detailJsonObject: JSONObject) : MovieDetailApiData {
        val id = detailJsonObject.getLong("id")
        val backdropPath = detailJsonObject.getString("backdrop_path")
        val title = detailJsonObject.getString("title")
        val tagline = detailJsonObject.getString("tagline")
        val overview = detailJsonObject.getString("overview")

        val genres = detailJsonObject.getJSONArray("genres")
        val genre = parseGenreListJsonArray(genres)

        return MovieDetailApiData(
            id = id,
            backdropPath = backdropPath,
            title = title,
            genres = genre,
            tagline = tagline,
            overview = overview
        )
    }

    private fun parseGenreListJsonArray(responseJsonArray: JSONArray): MutableList<String> {
        val genreList = mutableListOf<String>()

        for(index in 0 until responseJsonArray.length()){
            val detailJsonObject = (responseJsonArray[index] as? JSONObject) ?: continue
            val genre = detailJsonObject.getString("name")

            genreList.add(genre)
        }

        return genreList;
    }

    private fun displayToScreenMovieDetail() {
        posterImgLoader.loadPosterImg(
            context = backdropPathImageView.context,
            url = movieDetailData.backdropPath.orEmpty(),
            target = backdropPathImageView
        )
        titleTextView.text = movieDetailData.title
        genreTextView.text = movieDetailData.genres[0]
        taglineTextView.text = movieDetailData.tagline
        overviewTextView.text = movieDetailData.overview
    }

    private fun getSavedMovieDetailId(): Long {
        val sharedPreferences = getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(ID_KEY, 550)
    }
}