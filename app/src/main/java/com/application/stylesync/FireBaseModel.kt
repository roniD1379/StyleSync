package com.application.stylesync.Model

import com.example.myapp.firestore.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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
                val topic = document.getString("topic") ?: ""
                Post(content, imageUri, topic, color, userId)
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

    fun updatePost(postId: String, newPost: Post) {
        postsCollection.document(postId).set(newPost).addOnSuccessListener {
            println("DocumentSnapshot successfully written!")
        }.addOnFailureListener { e ->
            println("Error writing document: $e")
        }
    }

    suspend fun getPostsOfUser(userId: String): List<Post> {
        val querySnapshot = postsCollection.whereEqualTo("userId", userId).get().await()
        return querySnapshot.documents.map { document ->
            val id = document.id
            val imageUri = document.getString("imageUri") ?: ""
            val content = document.getString("content") ?: ""
            val color = document.getString("color") ?: ""
            val topic = document.getString("topic") ?: ""
            val userId = document.getString("userId") ?: ""
            Post(content, imageUri, topic, color, userId)
        }
    }
}