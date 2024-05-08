package com.application.stylesync.fragments.edit_post

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.application.stylesync.MainActivity
import com.application.stylesync.FirestoreManager
import com.application.stylesync.Post
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class EditPostViewModel : ViewModel() {

    fun updatePost(uri: Uri, post: Post, callback: () -> Unit) {
        if (uri != Uri.EMPTY) {
            val storageReference = FirebaseStorage.getInstance().reference
            val ref: StorageReference = storageReference.child("images/" + UUID.randomUUID().toString())
            ref.putFile(uri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    post.imageUri = uri.toString()
                    FirestoreManager().updatePost(post)
                    callback()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(MainActivity(), "Failed " + e.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            FirestoreManager().updatePost(post)
            callback()
        }
    }

    fun deletePost(postId: String, callback: () -> Unit) {
        FirestoreManager().deletePost(postId)
        callback()
    }
}