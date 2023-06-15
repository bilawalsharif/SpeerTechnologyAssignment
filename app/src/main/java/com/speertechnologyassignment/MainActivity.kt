package com.speertechnologyassignment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.speertechnologyassignment.adapter.FollowersAdapter
import com.speertechnologyassignment.data.ResponseData
import com.speertechnologyassignment.data.User
import com.speertechnologyassignment.network.GitHubApiClient.gitHubService
import com.speertechnologyassignment.repositroy.UserRepository
import com.speertechnologyassignment.repositroy.UserViewModelFactory
import com.speertechnologyassignment.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel
    private var userName: EditText? = null
    private var btnSearched: Button? = null
    private var usernameTextView: TextView? = null
    private var nameTextView: TextView? = null
    private var descriptionTextView: TextView? = null
    private var followersTextView: TextView? = null
    private var followingTextView: TextView? = null
    private var avatarImageView: ImageView? = null
    private var tvNotFound: TextView? = null
    private var detailParentLayout: ConstraintLayout? = null
    private var rvFollowers: RecyclerView? = null
    private var scrollView: ScrollView? = null
    private lateinit var navController: NavController

    var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        val userRepository = UserRepository(gitHubService)
        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository)
        ).get(UserViewModel::class.java)

        val searchButton: Button = findViewById(R.id.btnSearched)
        searchButton.setOnClickListener {
            if (isValid()){
                dialog!!.setCancelable(false)
                dialog!!.setMessage("Processing ...")
                dialog!!.show()
                scrollView?.visibility=View.VISIBLE
                val username: String = userName?.text.toString()
                val name=username.replace("\\s".toRegex(), "")
                viewModel.fetchUser(name)
            }else{
                userName?.error = "Enter Name"
            }
        }

        viewModel.user.observe(this) { user ->
            dialog!!.dismiss()
            when (user) {
                is ResponseData.Success -> {
                    val user = user.data
                    // Update the UI with the user data
                    tvNotFound?.visibility=View.GONE
                    detailParentLayout?.visibility=View.VISIBLE
                    updateUI(user)
                    // Call the second API here
                       val name=user.login.replace("\\s".toRegex(), "")
                       viewModel.fetchFollowers(name)
                }
                is ResponseData.Error -> {
                    val exception = user.exception
                    tvNotFound?.visibility=View.VISIBLE
                    detailParentLayout?.visibility=View.GONE
                    Log.d("error", exception.toString())
                }
            }
        }
        viewModel.followers.observe(this) { user ->
           if (user.isNotEmpty()){
               setFollowersAdapter(user)
           }
        }
    }

    private fun setFollowersAdapter(user: List<User>) {
        val followersAdapter= FollowersAdapter(user,itemOnClickFollowers)
        rvFollowers?.adapter=followersAdapter
    }

    private fun initViews() {
        dialog = ProgressDialog(this)
        userName =findViewById(R.id.etUserName);
        btnSearched = findViewById(R.id.btnSearched)
        avatarImageView = findViewById(R.id.avatar_image)
        usernameTextView = findViewById(R.id.username_text)
        nameTextView = findViewById(R.id.name_text)
        descriptionTextView = findViewById(R.id.description_text)
        followersTextView =findViewById(R.id.followers_text)
        followingTextView = findViewById(R.id.following_text)
        tvNotFound = findViewById(R.id.tvNotFound)
        detailParentLayout = findViewById(R.id.includeLayout)
        rvFollowers = findViewById(R.id.rvFollowers)
        scrollView = findViewById(R.id.scrollView)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun isValid(): Boolean {
        return userName?.text.toString().isNotEmpty()
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
    private val itemOnClickFollowers: (String,View) -> Unit = { name,view ->
        scrollView?.visibility=View.GONE
       // btnSearched?.visibility=View.GONE
       // userName?.visibility=View.GONE
        val bundle = bundleOf("name" to name)
      //  view.findNavController().navigate(R.id.followersFragment, bundle)

        navController.navigate(R.id.followersFragment,bundle)
    }

}
