package com.application.stylesync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.stylesync.dao.AppLocalDatabase
import java.util.concurrent.Executors

class DbModel private constructor() {

    enum class LoadingState {
        LOADING,
        LOADED
    }

    private val database = AppLocalDatabase.db
    private var executor = Executors.newSingleThreadExecutor()
    private val firestoreManager = FirestoreManager()
    private val posts: LiveData<MutableList<Post>>? = null
    val postsListLoadingState: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.LOADED)
    companion object {
        val instance: DbModel = DbModel()
    }

    interface GetAllPostsListener {
        fun onComplete(posts: List<Post>)
    }

    fun getAllPosts(callback: () -> Unit = {}): LiveData<MutableList<Post>> {
        refreshPosts()
        callback()
        return posts ?: database.postDao().getAll()
    }

    fun getPostsOfUser(userId: String): LiveData<MutableList<Post>> {
        refreshPosts()
        return database.postDao().getByUserId(userId)
    }

    fun refreshPosts() {
        val lastUpdated: Long = Post.lastUpdated
        postsListLoadingState.value = LoadingState.LOADING

        firestoreManager.getAllPosts(lastUpdated) { list ->
            executor.execute {
                var time = lastUpdated
                for (post in list) {
                    database.postDao().insert(post)

                    post.lastUpdated?.let {
                        if (time < it)
                            time = post.lastUpdated ?: System.currentTimeMillis()
                    }
                }
                Post.lastUpdated = time
                postsListLoadingState.postValue(LoadingState.LOADED)
            }
        }
    }

    fun addPost(post: Post, callback: () -> Unit) {
        firestoreManager.addNewPost(post) {
            refreshPosts()
            callback()
        }
    }

    fun deletePost(post: Post, callback: () -> Unit) {
        firestoreManager.deletePost(post.id) {
            refreshPosts()
            callback()
        }
    }

    fun updatePost(post: Post, callback: () -> Unit) {
        firestoreManager.updatePost(post) {
            refreshPosts()
            callback()
        }
    }


}