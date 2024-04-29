package com.application.stylesync.fragments.register

import androidx.lifecycle.ViewModel
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.FirebaseAuthManagerInterface

class RegisterViewModel : ViewModel() {

    fun registerUser(
        email: String,
        username: String,
        password: String,
        topic: String,
        themeColor: String,
        f: FirebaseAuthManagerInterface
    ) {
        if (email.trim().isEmpty() ||
            username.trim().isEmpty() ||
            password.trim().isEmpty()
        ) {
            f.failure("Enter all data!")
        } else {
            FirebaseAuthManager().registerUser(email, username, password, topic, themeColor, f)
        }
    }


}