package com.application.stylesync.fragments.create_new_post

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.application.stylesync.MainActivity
import com.application.stylesync.Model.FirestoreManager
import com.example.myapp.firestore.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class CreateNewPostViewModel : ViewModel() {
    private fun createNewPost(content: String, imageUri: String, topic: String, color: String, callback: () -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val post = Post(content, imageUri, topic, color, userId);
        FirestoreManager().addNewPost(post);
        callback()
    }

    fun uploadPost(file: Uri, content: String,topic: String, color: String, callback: () -> Unit) {
        val storageReference = FirebaseStorage.getInstance().reference
        val ref: StorageReference = storageReference.child("images/" + UUID.randomUUID().toString())
        ref.putFile(file).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { uri ->
                createNewPost(content, uri.toString(), topic, color, callback)
            }
        }.addOnFailureListener { e ->
            Toast.makeText(MainActivity(), "Failed " + e.message, Toast.LENGTH_SHORT).show()
        }
    }
}