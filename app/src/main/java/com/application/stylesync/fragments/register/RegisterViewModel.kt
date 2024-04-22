package com.application.stylesync.fragments.register

import androidx.lifecycle.ViewModel
import com.application.stylesync.Model.FirebaseAuthManager
import com.application.stylesync.Model.FirebaseAuthManagerInterface

class RegisterViewModel : ViewModel() {

    public fun registerUser(email: String,
                     username: String,
                     password: String,
                     topic: String,
                     themeColor: String,
                     f: FirebaseAuthManagerInterface
    ) {
        FirebaseAuthManager().registerUser(email, username, password, topic, themeColor, f)
    }

}