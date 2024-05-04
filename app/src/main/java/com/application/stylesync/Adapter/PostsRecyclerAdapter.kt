package com.application.stylesync.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.stylesync.R
import com.example.myapp.firestore.Post

class PostsRecyclerAdapter(var posts: List<Post>) : RecyclerView.Adapter<PostViewHolder>() {

    var listener: OnPostClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_layout_row, parent, false)
        return PostViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
       return posts.size ?: 0
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }
}

interface OnPostClickListener {
    fun onItemClick(position: Int) // post
    fun onPostClicked(post: Post?)
}