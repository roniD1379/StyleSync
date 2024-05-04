package com.application.stylesync

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class FirebaseAuthManager {
    private val USERS_REF = "users";
    private val usersCollection = FirebaseFirestore.getInstance().collection(USERS_REF)

    object CURRENT_USER {
        var uid: String = ""
        var uri: String = ""
        var email: String = ""
        var username: String = ""
        var style: String = ""
        var color: String = ""
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        CURRENT_USER.uid = ""
        CURRENT_USER.uri = ""
        CURRENT_USER.email = ""
        CURRENT_USER.username = ""
        CURRENT_USER.style = ""
        CURRENT_USER.color = ""
    }

    // Edit profile without image
    fun updateCurrentUser(f: FirebaseAuthManagerInterface) {
        usersCollection
            .document(CURRENT_USER.uid)
            .set(CURRENT_USER)
            .addOnFailureListener { it.localizedMessage?.let { it1 -> f.failure(it1) } }
            .addOnSuccessListener { f.success() }
    }

    // Edit profile with image
    fun updateCurrentUserWithImage(uri: Uri, f: FirebaseAuthManagerInterface) {
        val storageReference = FirebaseStorage.getInstance()
            .getReference(USERS_REF + "/" + CURRENT_USER.uid)
        storageReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                storageReference.getDownloadUrl().addOnSuccessListener { url: Uri ->
                    CURRENT_USER.uri = url.toString()
                    updateCurrentUser(f)
                }.addOnFailureListener {
                    it.localizedMessage?.let { it1 -> f.failure(it1) }
                }
            }.addOnFailureListener {
                it.localizedMessage?.let { it1 -> f.failure(it1) }
            }
    }

    fun registerUser(
        email: String,
        username: String,
        password: String,
        style: String,
        color: String,
        f: FirebaseAuthManagerInterface
    ) {
        Firebase.auth
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (Firebase.auth.currentUser != null) {
                    CURRENT_USER.uid = Firebase.auth.currentUser!!.uid
                    CURRENT_USER.uri = ""
                    CURRENT_USER.email = email
                    CURRENT_USER.username = username
                    CURRENT_USER.style = style
                    CURRENT_USER.color = color
                    registerUserOnFirestore(f)
                }
            }.addOnFailureListener {
                it.localizedMessage?.let { it1 -> f.failure(it1) }
            }
    }

    private fun registerUserOnFirestore(f: FirebaseAuthManagerInterface) {
        usersCollection
            .document(CURRENT_USER.uid)
            .set(CURRENT_USER)
            .addOnFailureListener { it.localizedMessage?.let { it1 -> f.failure(it1) } }
            .addOnSuccessListener { f.success() }
    }

    fun loginUser(email: String, password: String, f: FirebaseAuthManagerInterface) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                setUpUserFromFirestore(f)
            }.addOnFailureListener {
                it.localizedMessage?.let { it1 -> f.failure(it1) }
            }
    }

    fun setUpUserFromFirestore(f: FirebaseAuthManagerInterface) {
        FirebaseAuth.getInstance().currentUser?.let {
            usersCollection
                .document(it.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<CURRENT_USER>()
                    if (user != null) {
                        CURRENT_USER.uid = CURRENT_USER.uid
                        CURRENT_USER.uri = CURRENT_USER.uri
                        CURRENT_USER.email = CURRENT_USER.email
                        CURRENT_USER.username = CURRENT_USER.username
                        CURRENT_USER.style = CURRENT_USER.style
                        CURRENT_USER.color = CURRENT_USER.color
                        f.success()
                    }
                }
                .addOnFailureListener { it.localizedMessage?.let { it1 -> f.failure(it1) } }
        }
    }
}