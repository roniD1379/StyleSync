package com.application.stylesync.fragments.edit_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.FirebaseAuthManagerInterface

class EditProfileViewModel : ViewModel() {
    fun updateUser(username: String, topic: String, themeColor: String, f: FirebaseAuthManagerInterface) {
        if (username.trim().isEmpty()) {
            f.failure("Enter a username!")
        } else {
            FirebaseAuthManager.CURRENT_USER.username = username.trim()
            FirebaseAuthManager.CURRENT_USER.topic = topic.trim()
            FirebaseAuthManager.CURRENT_USER.themeColor = themeColor.trim()
            FirebaseAuthManager().updateCurrentUser(f)
        }
    }
    fun updateUser(uri: Uri, username: String, topic: String, themeColor: String, f: FirebaseAuthManagerInterface) {
        if (username.trim().isEmpty()) {
            f.failure("Enter a username!")
        } else {
            FirebaseAuthManager.CURRENT_USER.username = username.trim()
            FirebaseAuthManager.CURRENT_USER.topic = topic.trim()
            FirebaseAuthManager.CURRENT_USER.themeColor = themeColor.trim()
            FirebaseAuthManager().updateCurrentUserWithImage(uri, f)
        }
    }

}