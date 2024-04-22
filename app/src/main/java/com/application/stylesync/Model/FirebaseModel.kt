package com.application.stylesync.Model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class Post (id: String, title:String,content:String)

class FirestoreManager {

    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("posts")

    suspend fun getAllPosts(): List<Post> {
        val querySnapshot = postsCollection.get().await()
        return querySnapshot.documents.map { document ->
            val id = document.id
            val title = document.getString("title") ?: ""
            val content = document.getString("content") ?: ""
            Post(id, title, content)
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

    suspend fun updatePost(postId: String, newPost: Post) {
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
            val title = document.getString("title") ?: ""
            val content = document.getString("content") ?: ""
            Post(id, title, content)
        }
    }
}
