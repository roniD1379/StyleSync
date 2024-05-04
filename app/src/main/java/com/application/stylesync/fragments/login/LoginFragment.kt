package com.application.stylesync.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.FirebaseAuthManagerInterface
import com.application.stylesync.R
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    private lateinit var mViewModel: LoginViewModel

    private lateinit var tvRegister: TextView;
    private lateinit var btnLogin: Button;
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        findAllViewsById(view)
        setAllOnClicks(view)
        loginIfRemember(view)
        return view;
    }

    private fun loginIfRemember(view: View) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            val myObject = object : FirebaseAuthManagerInterface {
                override fun success() {
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
                }

                override fun failure(message: String) {
                    Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
                }
            }
            FirebaseAuthManager().setUpUserFromFirestore(myObject)
        }
    }
    private fun findAllViewsById(view: View) {
        tvRegister = view.findViewById(R.id.tvRegister)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
    }

    private fun setAllOnClicks(view: View) {
        tvRegister.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_loginFragment_to_registerFragment)
        }


        val myObject = object : FirebaseAuthManagerInterface {
            override fun success() {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
            }

            override fun failure(message: String) {
                Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
            }
        }

        btnLogin.setOnClickListener {
            mViewModel.loginUser(
                email = etEmail.text.toString().trim(),
                password = etPassword.text.toString().trim(),
                f = myObject
            )
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

}