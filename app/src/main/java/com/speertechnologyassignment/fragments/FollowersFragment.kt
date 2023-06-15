package com.speertechnologyassignment.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.speertechnologyassignment.R
import com.speertechnologyassignment.data.ResponseData
import com.speertechnologyassignment.data.User
import com.speertechnologyassignment.network.GitHubApiClient
import com.speertechnologyassignment.repositroy.UserRepository
import com.speertechnologyassignment.repositroy.UserViewModelFactory
import com.speertechnologyassignment.viewmodel.UserViewModel

class FollowersFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var viewModel: UserViewModel
    private var usernameTextView: TextView? = null
    private var nameTextView: TextView? = null
    private var descriptionTextView: TextView? = null
    private var followersTextView: TextView? = null
    private var followingTextView: TextView? = null
    private var avatarImageView: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_followers, container, false)
        initViews()

        return rootView
    }

    private fun initViews() {
        val name = arguments?.getString("name")
        avatarImageView = rootView.findViewById(R.id.avatar_image)
        usernameTextView = rootView.findViewById(R.id.username_text)
        nameTextView = rootView.findViewById(R.id.name_text)
        descriptionTextView = rootView.findViewById(R.id.description_text)
        followersTextView = rootView.findViewById(R.id.followers_text)
        followingTextView = rootView.findViewById(R.id.following_text)

        val userRepository = UserRepository(GitHubApiClient.gitHubService)
        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository)
        ).get(UserViewModel::class.java)

        if (name!=null)
        viewModel.fetchUser(name.toString())

        viewModel.user.observe(requireActivity()) { user ->
            when (user) {
                is ResponseData.Success -> {
                    val user = user.data
                    updateUI(user)
                }
                is ResponseData.Error -> {
                    val exception = user.exception
                    Log.d("error", exception.toString())
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(user: User) {
        // Bind the user data to the views
        // You can use any image loading library to load the avatar image
        // For simplicity, we'll assume the avatarUrl is a valid URL
        avatarImageView?.let { Glide.with(this).load(user.avatar_url).into(it) }
        usernameTextView?.text = user.login
        nameTextView?.text = "Name " + user.name
        descriptionTextView?.text = "Description " + user.bio
        followersTextView?.text = "Followers " + user.followers.toString()
        followingTextView?.text = "Following " + user.following.toString()

    }
}