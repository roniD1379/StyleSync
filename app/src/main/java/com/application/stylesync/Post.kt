package com.example.myapp.firestore

data class Post(
    val content: String,
    val imageUri: String,
    val topic: String,
    val color: String,
    val userId: String
) {
}