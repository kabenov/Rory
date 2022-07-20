package kz.example.myapp.content

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kz.example.myapp.R
import kz.example.myapp.authorization.LoginActivity
import kz.example.myapp.authorization.NAME_SHARED_PREFERENCES
import kz.example.myapp.content.data.models.MovieApiData
import kz.example.myapp.network.API_KEY
import kz.example.myapp.network.TAG
import kz.example.myapp.network.ID_KEY
import kz.example.myapp.network.apiClient
import kz.example.myapp.presentation.view.MovieCardAdapter
import kz.example.myapp.presentation.view.OnMovieListener
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class HomePageActivity : AppCompatActivity() {
    private lateinit var nameTextView: TextView
    private lateinit var userEmail: String
//    private lateinit var exitButton: Button

    private lateinit var categoryItem1: LinearLayout
    private lateinit var movieTitleTextView1: TextView
    private lateinit var forwardIconImageView1: ImageView
    private lateinit var movieListRecyclerView1: RecyclerView

    private lateinit var categoryItem2: LinearLayout
    private lateinit var movieTitleTextView2: TextView
    private lateinit var forwardIconImageView2: ImageView
    private lateinit var movieListRecyclerView2: RecyclerView

    private val adapter1 = MovieCardAdapter(object: OnMovieListener{
        override fun onMovieClick(position: Long) {
            saveMovieIdToSharedPreference(position)
            toMovieDetailActivity()
        }

        override fun onCategoryClick(categoryName: String) {

        }

    })
    private val adapter2 = MovieCardAdapter(object: OnMovieListener{
        override fun onMovieClick(position: Long) {
            saveMovieIdToSharedPreference(position)
            toMovieDetailActivity()
        }

        override fun onCategoryClick(categoryName: String) {

        }

    })

    private var dataList1: MutableList<MovieApiData> = mutableListOf()
    private var dataList2: MutableList<MovieApiData> = mutableListOf()

    private lateinit var bottomNavigationView: BottomNavigationView

    private val fragment = HomePageFragment(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        onBind()

        bottomNavigationView.selectedItemId = R.id.home_page_menu

        loadApiData()


//        exitButton.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_page_menu -> {}
                R.id.search_menu -> {startActivity(Intent(this, SearchActivity::class.java))}
                //R.id.likes_menu -> {startActivity(Intent(this, LikesActivity::class.java))}
                R.id.profile_menu -> {startActivity(Intent(this, ProfileActivity::class.java))}
            }
            true
        }
    }

    private fun onBind() {
        nameTextView = findViewById(R.id.home_page_activity_text_view_welcome_message)
        userEmail = getSavedUserEmail()
//        exitButton = findViewById(R.id.activity_temp_button_exit)
        nameTextView.text = getString(R.string.welcome_message_fmt, userEmail)

        categoryItem1 = findViewById(R.id.movie_category_item_popular)
        movieTitleTextView1 = categoryItem1.findViewById(R.id.category_item_text_view_movie_category_title)
//        forwardIconImageView1 = categoryItem1.findViewById(R.id.category_item_image_view_forward_icon)
        movieListRecyclerView1 = categoryItem1.findViewById(R.id.category_item_recycler_view_list_of_cards)
        movieListRecyclerView1.adapter = adapter1

        categoryItem2 = findViewById(R.id.movie_category_item_trending)
        movieTitleTextView2 = categoryItem2.findViewById(R.id.category_item_text_view_movie_category_title)
//        forwardIconImageView2 = categoryItem2.findViewById(R.id.category_item_image_view_forward_icon)
        movieListRecyclerView2 = categoryItem2.findViewById(R.id.category_item_recycler_view_list_of_cards)
        movieListRecyclerView2.adapter = adapter2

        bottomNavigationView = findViewById(R.id.home_page_activity_bottom_navigation_view)
    }

    private fun getSavedUserEmail(): String {
        val userEmail = intent.getStringExtra("email_id")

        return userEmail ?: "Anonymous"
//        val sharedPreferences = getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
//        return sharedPreferences.getString(USERNAME_KEY, "Anonymous") ?: "Anonymous"
    }

    private fun loadApiData() {
        getPopularMovies()
        getTrendingMovies()
    }

    private fun getPopularMovies() {
        apiClient.getPopularMovies(API_KEY, 1).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body()!!
                val responseJsonArray =JSONObject(responseBody.string())

                dataList1 = parsePageJsonObject(responseJsonArray)

                displayToScreenPopularMovies()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, t.localizedMessage ?: "Failure Occurred")
            }
        })
    }

    private fun getTrendingMovies() {
        apiClient.getTrendingMovies(API_KEY).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body()!!
                val responseJsonArray =JSONObject(responseBody.string())

                dataList2 = parsePageJsonObject(responseJsonArray)

                displayToScreenTrendingMovies()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, t.localizedMessage ?: "Failure Occurred")
            }
        })
    }

    private fun parsePageJsonObject(responseJsonObject: JSONObject): MutableList<MovieApiData>{
        val movieListJsonArray = responseJsonObject.getJSONArray("results")

        return parseMovieListJsonArray(movieListJsonArray)
    }

    private fun parseMovieListJsonArray(responseJsonArray: JSONArray): MutableList<MovieApiData> {
        val movieList = mutableListOf<MovieApiData>()

        for(index in 0 until responseJsonArray.length()){
            val infoJsonObject = (responseJsonArray[index] as? JSONObject) ?: continue
            val infoData = parseMovieJsonObject(infoJsonObject)

            movieList.add(infoData)
        }

        return movieList;
    }

    private fun parseMovieJsonObject(infoJsonObject: JSONObject): MovieApiData{
        val id = infoJsonObject.getLong("id")
        val title = try {
            infoJsonObject.getString("title")
        }
        catch (e: Exception) {
            infoJsonObject.getString("name")
        }
        val posterPath = infoJsonObject.getString("poster_path")

        return MovieApiData(
                id = id,
                title = title,
                poster_path = posterPath
        )
    }

    private fun displayToScreenPopularMovies() {
        adapter1.setList(dataList1)
        movieTitleTextView1.text = "Popular"
    }

    private fun displayToScreenTrendingMovies() {
        adapter2.setList(dataList2)
        movieTitleTextView2.text = "Trending"
    }

    private fun toMovieDetailActivity(){
        val intent = Intent(this, MovieDetailActivity::class.java)
        startActivity(intent)
    }

    private fun saveMovieIdToSharedPreference(id: Long) {
        val sharedPreferences = getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putLong(ID_KEY, id).apply()
    }
}
