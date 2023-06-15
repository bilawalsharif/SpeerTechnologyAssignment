package com.speertechnologyassignment.data

data class User(
    val avatar_url: String,
    val login: String,
    val name: String,
    val bio: String,
    val followers: Int,
    val following: Int
)
