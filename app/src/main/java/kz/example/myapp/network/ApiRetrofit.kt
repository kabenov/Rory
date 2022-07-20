package kz.example.myapp.network

import kz.example.myapp.content.data.ApiClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(API_BASE_URL)
    .addConverterFactory(JacksonConverterFactory.create())
    .build()

val apiClient = retrofit.create(ApiClient::class.java)