package com.application.stylesync

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Post(
    var content: String,
    var imageUri: String,
    var style: String,
    var color: String,
    var userId: String,
    var id: String
) {
}

@Parcelize
data class PostParcelable(
    val content: String,
    val imageUri: String,
    val style: String,
    val color: String,
    val userId: String,
    val id: String
) : Parcelable {
    constructor(post: Post) : this(
        post.content,
        post.imageUri,
        post.style,
        post.color,
        post.userId,
        post.id
    )
}