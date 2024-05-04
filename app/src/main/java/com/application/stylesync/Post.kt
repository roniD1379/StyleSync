package com.application.stylesync

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Post(
    var content: String,
    var imageUri: String,
    var topic: String,
    var color: String,
    var userId: String,
    var id: String
) {
}

@Parcelize
data class PostParcelable(
    val content: String,
    val imageUri: String,
    val topic: String,
    val color: String,
    val userId: String,
    val id: String
) : Parcelable {
    constructor(post: Post) : this(
        post.content,
        post.imageUri,
        post.topic,
        post.color,
        post.userId,
        post.id
    )
}