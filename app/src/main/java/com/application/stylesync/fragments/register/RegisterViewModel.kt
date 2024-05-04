package com.application.stylesync.fragments.register

import androidx.lifecycle.ViewModel
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.FirebaseAuthManagerInterface

class RegisterViewModel : ViewModel() {

    fun registerUser(
        email: String,
        username: String,
        password: String,
        style: String,
        color: String,
        f: FirebaseAuthManagerInterface
    ) {
        if (email.trim().isEmpty() ||
            username.trim().isEmpty() ||
            password.trim().isEmpty()
        ) {
            f.failure("Please fill all the required fields")
        } else {
            FirebaseAuthManager().registerUser(email, username, password, style, color, f)
        }
    }


}