package com.application.stylesync

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings

class FirestoreManager {

    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("posts")


    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {  })
        }
        db.firestoreSettings = settings
    }

    fun getAllPosts(since: Long = 0, callback: (List<Post>) -> Unit) {
        postsCollection.whereGreaterThanOrEqualTo(Post.LAST_UPDATED, Timestamp(since, 0)).get().addOnSuccessListener {
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

    fun addNewPost(post: Post, callback: () -> Unit = {}) {
        postsCollection.add(post.json).addOnSuccessListener {
            callback()
        }.addOnFailureListener { e ->
            println("Error adding document: $e")
        }
    }

    fun deletePost(postId: String, callback: () -> Unit = {}) {
        postsCollection.document(postId).delete().addOnSuccessListener {
            callback()
        }.addOnFailureListener { e ->
            println("Error deleting document: $e")
        }
    }

    fun updatePost(post: Post, callback: () -> Unit = {}) {
        postsCollection.document(post.id).set(post.json).addOnSuccessListener {
           callback()
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
                val topic = document.getString("topic") ?: ""
                Post(content, imageUri, topic, color, userId, id)
            }
            callback(posts)
        }
    }
}
