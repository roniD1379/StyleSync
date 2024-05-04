package com.application.stylesync.fragments.edit_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.FirebaseAuthManagerInterface

class EditProfileViewModel : ViewModel() {
    fun updateUser(username: String, style: String, color: String, f: FirebaseAuthManagerInterface) {
        if (username.trim().isEmpty()) {
            f.failure("Please enter username")
        } else {
            FirebaseAuthManager.CURRENT_USER.username = username.trim()
            FirebaseAuthManager.CURRENT_USER.style = style.trim()
            FirebaseAuthManager.CURRENT_USER.color = color.trim()
            FirebaseAuthManager().updateCurrentUser(f)
        }
    }
    fun updateUser(uri: Uri, username: String, style: String, color: String, f: FirebaseAuthManagerInterface) {
        if (username.trim().isEmpty()) {
            f.failure("Please enter username")
        } else {
            FirebaseAuthManager.CURRENT_USER.username = username.trim()
            FirebaseAuthManager.CURRENT_USER.style = style.trim()
            FirebaseAuthManager.CURRENT_USER.color = color.trim()
            FirebaseAuthManager().updateCurrentUserWithImage(uri, f)
        }
    }

}