package com.application.stylesync.Model

interface FirebaseAuthManagerInterface {
    fun success();
    fun failure(message: String);
}