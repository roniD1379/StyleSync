package com.application.stylesync.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.application.stylesync.DbModel
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.FirestoreManager
import com.application.stylesync.Post

class HomeViewModel : ViewModel() {

    var posts: LiveData<MutableList<Post>>? = null
    fun setPosts(callback: () -> Unit) {
        posts = DbModel.instance.getAllPosts() {
            callback()
        }
    }

    fun getFilteredPosts(): MutableList<Post> {
        val color = FirebaseAuthManager.CURRENT_USER.color
        val style = FirebaseAuthManager.CURRENT_USER.style
        return posts?.value?.filter { it.color == color && it.style == style }?.toMutableList() ?: mutableListOf()
    }
}
