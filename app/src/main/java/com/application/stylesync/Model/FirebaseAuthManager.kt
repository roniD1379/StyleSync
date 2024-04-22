package com.application.stylesync.Model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class FirebaseAuthManager {
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")

    object CURRENT_USER {
        var uid: String = ""
        var uri: String = ""
        var email: String = ""
        var username: String = ""
        var topic: String = ""
        var themeColor: String = ""
    }

    public fun registerUser(
        email: String,
        username: String,
        password: String,
        topic: String,
        themeColor: String,
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
                    CURRENT_USER.topic = topic
                    CURRENT_USER.themeColor = themeColor
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

    public fun loginUser(email: String, password: String, f: FirebaseAuthManagerInterface) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                setUpUserFromFirestore(f)
            }.addOnFailureListener {
                it.localizedMessage?.let { it1 -> f.failure(it1) }
            }
    }

    private fun setUpUserFromFirestore(f: FirebaseAuthManagerInterface) {
        if (CURRENT_USER.uid != "") {
            usersCollection
                .document(CURRENT_USER.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<CURRENT_USER>()
                    if (user != null) {
                        CURRENT_USER.uid = user.uid
                        CURRENT_USER.uri = user.uri
                        CURRENT_USER.email = user.email
                        CURRENT_USER.username = user.username
                        CURRENT_USER.topic = user.topic
                        CURRENT_USER.themeColor = user.themeColor
                        f.success()
                    }
                }
                .addOnFailureListener { it.localizedMessage?.let { it1 -> f.failure(it1) } }
        }
    }
}