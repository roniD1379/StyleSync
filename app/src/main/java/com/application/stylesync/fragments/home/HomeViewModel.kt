package com.application.stylesync.fragments.home

import androidx.lifecycle.ViewModel
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.FirestoreManager
import com.application.stylesync.Post

class HomeViewModel : ViewModel() {

    var posts :  MutableList<Post> = mutableListOf()
    fun setPosts(callback: () -> Unit) {
        FirestoreManager().getAllPosts() {
            posts = it.toMutableList()
            callback()
        }
    }

    fun getFilteredPosts(): MutableList<Post> {
        val color = FirebaseAuthManager.CURRENT_USER.color
        val style = FirebaseAuthManager.CURRENT_USER.style
        return posts.filter { it.color == color && it.style == style }.toMutableList()
    }
}
