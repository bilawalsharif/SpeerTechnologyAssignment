package com.speertechnologyassignment.repositroy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.speertechnologyassignment.data.ResponseData
import com.speertechnologyassignment.data.User
import com.speertechnologyassignment.network.GitHubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class UserRepository(private val apiService: GitHubService) {

    fun getUser(username: String): MutableLiveData<ResponseData<User?>> {
        val userLiveData = MutableLiveData<ResponseData<User?>>()
        val call = apiService.getUser(username)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    userLiveData.postValue(ResponseData.Success(user))
                } else {
                    val errorBody = response.errorBody()?.string()
                    userLiveData.postValue(ResponseData.Error(Exception(errorBody)))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                userLiveData.postValue(ResponseData.Error(t as Exception))
            }
        })
        return userLiveData
    }
    fun getFollowers(username: String): MutableLiveData<List<User?>?> {
        val userLiveData = MutableLiveData<List<User?>?>()
        val call = apiService.getFollowers(username)
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    userLiveData.postValue(user)
                } else {
                    val errorBody = response.errorBody()?.string()
                   // userLiveData.postValue(ResponseData.Error(Exception(errorBody)))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                //userLiveData.postValue(ResponseData.Error(t as Exception))
            }
        })
        return userLiveData
    }
}


