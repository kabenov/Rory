package kz.example.myapp.content

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kz.example.myapp.R
import kz.example.myapp.authorization.NAME_SHARED_PREFERENCES
import kz.example.myapp.content.data.models.MovieApiData
import kz.example.myapp.network.API_KEY
import kz.example.myapp.network.ID_KEY
import kz.example.myapp.network.TAG
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

class SearchActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var searchButton: Button
    private lateinit var searchEditTextView: EditText
    private lateinit var movieListRecyclerView1: RecyclerView

    private val adapter = MovieCardAdapter(object: OnMovieListener {
        override fun onMovieClick(position: Long) {
            saveMovieIdToSharedPreference(position)
            toMovieDetailActivity()
        }

        override fun onCategoryClick(categoryName: String) {

        }

    })

    private var dataList1: MutableList<MovieApiData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        onBind()

        bottomNavigationView.selectedItemId = R.id.search_menu

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_page_menu -> {startActivity(Intent(this, HomePageActivity::class.java))}
                R.id.search_menu -> {}
                //R.id.likes_menu -> {startActivity(Intent(this, LikesActivity::class.java))}
                R.id.profile_menu -> {startActivity(Intent(this, ProfileActivity::class.java))}
            }
            true
        }

        searchButton.setOnClickListener {
            val searchTitle = searchEditTextView.text.toString()



            loadApiData(searchTitle)
        }


    }

    private fun onBind() {
        bottomNavigationView = findViewById(R.id.search_activity_bottom_navigation_view)
        searchButton = findViewById(R.id.search_button)
        searchEditTextView = findViewById(R.id.edit_text_search_title)
        movieListRecyclerView1 = findViewById(R.id.search_item_recycler_view_list_of_cards)

        movieListRecyclerView1.adapter = adapter
    }

    private fun loadApiData(searchTitle: String) {
        apiClient.getSearchMovies(API_KEY, 1, searchTitle).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body()!!
                val responseJsonArray = JSONObject(responseBody.string())

                dataList1 = parsePageJsonObject(responseJsonArray)

                displayToScreenPopularMovies()
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
        adapter.setList(dataList1)
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