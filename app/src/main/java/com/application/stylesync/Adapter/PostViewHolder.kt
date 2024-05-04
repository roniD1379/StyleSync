package com.application.stylesync.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.stylesync.R
import com.example.myapp.firestore.Post
import com.squareup.picasso.Picasso

class PostViewHolder(itemView: View, private val listener: OnPostClickListener?) : RecyclerView.ViewHolder(itemView) {

    private var post: Post? = null
    private var imageView: ImageView? = null
    private var content: TextView? = null
    private var topic: TextView? = null
    private var color: TextView? = null

    init {
        imageView = itemView.findViewById(R.id.post_image)
        content = itemView.findViewById(R.id.post_content)
        color = itemView.findViewById(R.id.post_color)
        topic = itemView.findViewById(R.id.post_topic)

        itemView.setOnClickListener {
            listener?.onPostClicked(post)
            listener?.onItemClick(adapterPosition)
        }
    }

    fun bind(post: Post) {
        this.post = post
        content?.text = post.content
        topic?.text = post.topic
        color?.text = post.color

        Picasso.get().load(post.imageUri).placeholder(R.drawable.gallery).fit().into(imageView)
    }
}