package com.speertechnologyassignment.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GitHubApiClient {
    private const val BASE_URL = "https://api.github.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val gitHubService: GitHubService = retrofit.create(GitHubService::class.java)
}
