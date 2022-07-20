package kz.example.myapp.content

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kz.example.myapp.R
import kz.example.myapp.authorization.NAME_SHARED_PREFERENCES
import kz.example.myapp.authorization.USERNAME_KEY
import kz.example.myapp.content.data.models.ApiInfoData
import kz.example.myapp.network.apiClient
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TempActivity: AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var sizeTextView: TextView
    private lateinit var exitButton: Button
    private lateinit var parseButton: Button
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)

        initViews()

        nameTextView.text = getString(R.string.welcome_message_fmt, username)

        exitButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)

            sharedPreferences.edit().remove(USERNAME_KEY).apply()
            finish()
        }

        parseButton.setOnClickListener {
//            apiClient.getPopularMovies().enqueue(object: Callback<ResponseBody> {
//                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                    val responseBody = response.body()!!
//                    val responseJsonStrings = responseBody.string()
//                    val responseJsonArray = JSONArray(responseJsonStrings)
//
//                    val apiInfoDataList = parseInfoDataJsonArray(responseJsonArray)
//                    println(apiInfoDataList)
//
//                    sizeTextView.text = apiInfoDataList.size.toString()
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    Log.d(TAG, t.localizedMessage ?: "Failure occurred")
//                }
//            })
        }
    }

    private fun parseInfoDataJsonArray(
            responseJsonArray: JSONArray
    ): List<ApiInfoData> {

        val infoDataList = mutableListOf<ApiInfoData>()

        for(index in 0 until responseJsonArray.length()){
            val infoJsonObject = (responseJsonArray[index] as? JSONObject) ?: continue
            val infoData = parseInfoDataJsonObject(infoJsonObject)

            infoDataList.add(infoData)
        }

        return infoDataList;
    }

    private fun parseInfoDataJsonObject(
            infoJsonObject: JSONObject
    ): ApiInfoData{

        val id = infoJsonObject.getLong("id")
        val time = infoJsonObject.getLong("time")
        val text = infoJsonObject.getString("text")

        return ApiInfoData(
                id = id,
                time = time,
                text = text,
                image = null
        )
    }



    private fun initViews() {
        nameTextView = findViewById(R.id.activity_temp_text_view_welcome_message)
        sizeTextView = findViewById(R.id.activity_temp_text_view_size)
        exitButton = findViewById(R.id.activity_temp_button_exit)
        parseButton = findViewById(R.id.activity_temp_button_parse)
        username = getSavedUsername()
    }

    private fun getSavedUsername(): String {
        val sharedPreferences = getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        return sharedPreferences.getString(USERNAME_KEY, "Anonymous") ?: "Anonymous"
    }
}