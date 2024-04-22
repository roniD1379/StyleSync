package com.application.stylesync.fragments.register

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.application.stylesync.CustomSpinnerAdapter
import com.application.stylesync.Model.FirebaseAuthManager
import com.application.stylesync.Model.FirebaseAuthManagerInterface
import com.application.stylesync.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.Executor

class RegisterFragment : Fragment() {
    private var mViewModel: RegisterViewModel? = null
    private var mAuth: FirebaseAuth? = null

    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var spTopic: Spinner
    private lateinit var spColor: Spinner
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        findAllViewsById(view)
        setAllOnClicks(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        currentUser?.reload()
    }

    private fun setUpTopicSpinner (view: View) {
        spTopic = view.findViewById(R.id.spTopic)

        // Create an ArrayAdapter using the options list and a simple spinner layout
        val adapter = this.context?.let {
            CustomSpinnerAdapter(
                it,
                listOf("",)
            )
        }

        // Apply the adapter to the spinner
        spTopic.adapter = adapter
    }

    private fun findAllViewsById(view: View) {
        etEmail = view.findViewById(R.id.etEmail)
        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        setUpTopicSpinner(view)

        spColor = view.findViewById(R.id.spColor)
        btnRegister = view.findViewById(R.id.btnRegister)
        tvLogin = view.findViewById(R.id.tvLogin)
    }


    private fun setAllOnClicks(view: View) {
        val myObject = object : FirebaseAuthManagerInterface {
            override fun success() {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_homeFragment)
            }

            override fun failure(message: String) {
                Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
            }

        }

        tvLogin.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        })

        btnRegister.setOnClickListener({
            mViewModel?.registerUser(email = etEmail.text.toString().trim(),
                username = etUsername.text.toString().trim(),
                password = etPassword.text.toString().trim(),
                topic = "",
                themeColor = "",
                f = myObject)
            // Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        })
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