package com.example.myrecipes.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private const val BASE_URL = "https://kgtttq6tg9.execute-api.us-east-2.amazonaws.com/prod/"

    object RetrofitClient {

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    object ApiClient {
        private val retrofit: Retrofit = RetrofitClient.retrofit

        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}