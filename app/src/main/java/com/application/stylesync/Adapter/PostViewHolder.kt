package com.application.stylesync.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.stylesync.R
import com.application.stylesync.Post
import com.squareup.picasso.Picasso

class PostViewHolder(itemView: View, private val listener: OnPostClickListener?) : RecyclerView.ViewHolder(itemView) {

    private var post: Post? = null
    private var imageView: ImageView? = null
    private var content: TextView? = null

    init {
        content = itemView.findViewById(R.id.post_content)
        imageView = itemView.findViewById(R.id.post_image)
        itemView.setOnClickListener {
            listener?.onPostClicked(post)
        }
    }

    fun bind(post: Post) {
        this.post = post
        content?.text = post.content
        Picasso.get().load(post.imageUri).placeholder(R.drawable.gallery).fit().into(imageView)
    }
}