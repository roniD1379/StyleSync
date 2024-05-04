package com.application.stylesync.fragments.home

import androidx.lifecycle.ViewModel
import com.application.stylesync.Model.FirestoreManager
import com.example.myapp.firestore.Post

class HomeViewModel : ViewModel() {

    var posts :  MutableList<Post> = mutableListOf()
    fun setPosts(callback: () -> Unit) {
        FirestoreManager().getAllPosts() {
            posts = it.toMutableList()
            callback()
        }
    }
}
