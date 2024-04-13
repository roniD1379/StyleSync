package com.application.stylesync.activities.register

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.application.stylesync.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.Executor

class RegisterFragment : Fragment() {
    private var mViewModel: RegisterViewModel? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        currentUser?.reload()
    }

    // Sign up account
    fun createAccount(email: String?, password: String?) {
        mAuth!!.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener((this as Executor)) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val user = mAuth!!.currentUser
                    // updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // updateUI(null);
                }
            }
    }

    // Example Db usage: add user to collection
    var db = FirebaseFirestore.getInstance()
    fun addAccount(user: Any?) {

        // Add a new document with a generated ID
        db.collection("users")
            .add(user!!)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    ContentValues.TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error adding document", e) }
    }

    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }
}