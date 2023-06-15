package com.speertechnologyassignment.network

import com.speertechnologyassignment.data.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<User>
    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<User>>
}
