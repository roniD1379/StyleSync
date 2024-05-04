package com.application.stylesync.fragments.profile

import androidx.lifecycle.ViewModel
import com.application.stylesync.Model.FirestoreManager
import com.application.stylesync.Post
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel : ViewModel() {
    var posts :  MutableList<Post> = mutableListOf()
    fun setPosts(callback: () -> Unit) {
        FirestoreManager().getPostsOfUser(FirebaseAuth.getInstance().currentUser!!.uid) {
            posts = it.toMutableList()
            callback()
        }
    }
}