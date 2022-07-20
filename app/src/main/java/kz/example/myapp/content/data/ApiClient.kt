package kz.example.myapp.content.data

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET("/3/movie/popular?api_key=327221eaef545f243c01b94bd6d6ad1e")
    fun getPopularMovies(): Call<ResponseBody>

    @GET("/3/movie/popular")
    fun getPopularMovies(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Call<ResponseBody>

    @GET("/3/trending/all/day?api_key=327221eaef545f243c01b94bd6d6ad1e")
    fun getTrendingMovies(): Call<ResponseBody>

    @GET("/3/trending/all/day")
    fun getTrendingMovies(@Query("api_key") key: String): Call<ResponseBody>

    @GET("/3/movie/{id}?api_key=327221eaef545f243c01b94bd6d6ad1e")
    fun getMovieById(@Path("id") id: Long): Call<ResponseBody>

    @GET("/3/movie/{id}?")
    fun getMovieById(
        @Path("id") id: Long,
        @Query("api_key") key: String
    ): Call<ResponseBody>

    @GET("/3/search/movie/")
    fun getSearchMovies(
        @Query("api_key") key: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): Call<ResponseBody>
}