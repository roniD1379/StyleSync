package com.application.stylesync

interface FirebaseAuthManagerInterface {
    fun success();
    fun failure(message: String);
}