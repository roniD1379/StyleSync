package com.application.stylesync.Model

import com.application.stylesync.Post
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreManager {

    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("posts")

    fun getAllPosts(callback: (List<Post>) -> Unit) {
        postsCollection.get().addOnSuccessListener {
            val posts =  it.documents.map { document ->
                val id = document.id
                val imageUri = document.getString("imageUri") ?: ""
                val content = document.getString("content") ?: ""
                val userId = document.getString("userId") ?: ""
                val color = document.getString("color") ?: ""
                val style = document.getString("style") ?: ""
                Post(content, imageUri, style, color, userId, id)
            }
            callback(posts)
        }
    }

    fun addNewPost(post: Post) {
        postsCollection.add(post).addOnSuccessListener {
            println("DocumentSnapshot written with ID: ${it.id}")
        }.addOnFailureListener { e ->
            println("Error adding document: $e")
        }
    }

    fun deletePost(postId: String) {
        postsCollection.document(postId).delete().addOnSuccessListener {
            println("DocumentSnapshot successfully deleted!")
        }.addOnFailureListener { e ->
            println("Error deleting document: $e")
        }
    }

    fun updatePost(post: Post) {
        postsCollection.document(post.id).set(post).addOnSuccessListener {
            println("DocumentSnapshot successfully written!")
        }.addOnFailureListener { e ->
            println("Error writing document: $e")
        }
    }

    fun getPostsOfUser(userId: String, callback: (List<Post>) -> Unit) {
        postsCollection.whereEqualTo("userId", userId).get().addOnSuccessListener {
            val posts = it.documents.map { document ->
                val id = document.id
                val imageUri = document.getString("imageUri") ?: ""
                val content = document.getString("content") ?: ""
                val color = document.getString("color") ?: ""
                val style = document.getString("style") ?: ""
                Post(content, imageUri, style, color, userId, id)
            }
            callback(posts)
        }
    }
}
