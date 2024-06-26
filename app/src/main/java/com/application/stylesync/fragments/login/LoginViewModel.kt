package com.application.stylesync.fragments.login

import androidx.lifecycle.ViewModel
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.FirebaseAuthManagerInterface

class LoginViewModel : ViewModel() {
    fun loginUser(email: String,
                  password: String,
                  f: FirebaseAuthManagerInterface
    ) {
        if (email.trim().isEmpty() ||
            password.trim().isEmpty()) {
            f.failure("Please fill all the required fields")
        } else {
            FirebaseAuthManager().loginUser(email, password, f)
        }
    }
}