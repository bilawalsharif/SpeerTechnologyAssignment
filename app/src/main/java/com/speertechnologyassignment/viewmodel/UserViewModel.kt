package com.speertechnologyassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.speertechnologyassignment.data.ResponseData
import com.speertechnologyassignment.data.User
import com.speertechnologyassignment.repositroy.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<ResponseData<User>>()
    val user: LiveData<ResponseData<User>> get() = _user
    private val _followers = MutableLiveData<List<User>>()
    val followers: LiveData<List<User>> get() = _followers

    fun fetchUser(username: String) {
        userRepository.getUser(username).observeForever {
            _user.postValue(it as ResponseData<User>?)
        }
    }
    fun fetchFollowers(username: String) {
        userRepository.getFollowers(username).observeForever {
            _followers.postValue(it as List<User>?)
        }
    }
}

