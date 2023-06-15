package com.speertechnologyassignment.data

sealed class ResponseData<out T> {
    data class Success<out T>(val data: T) : ResponseData<T>()
    data class Error(val exception: Exception) : ResponseData<Nothing>()
}
