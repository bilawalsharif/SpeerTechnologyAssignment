package com.speertechnologyassignment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.speertechnologyassignment.R
import com.speertechnologyassignment.data.User

class FollowersAdapter(private val userList: List<User>,var itemOnClickFollowers: (String,View) -> Unit) : RecyclerView.Adapter<FollowersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.tvFollowers)
        private val parentView: ConstraintLayout = itemView.findViewById(R.id.parentView)

        fun bind(user: User) {
            usernameTextView.text =  user.login
            parentView.setOnClickListener {
                itemOnClickFollowers(user.login,parentView)
            }
        }
    }
}
