package com.application.stylesync.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.application.stylesync.FirebaseAuthManagerInterface
import com.application.stylesync.R
import com.google.firebase.auth.FirebaseAuth

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

    private var chosenTopic: String = "A" // todo: change to the first option
    private var chosenTheme: String = "A" // todo: change to the first option

    val topicOptions = arrayOf("A", "B", "C")
    val themeOptions = arrayOf("A", "B", "C")

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


    private fun setUpSpinners (view: View) {
        spTopic = view.findViewById(R.id.spTopic)
        spColor = view.findViewById(R.id.spColor)

        val topicAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, topicOptions)
        val themeAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, themeOptions)

        // Apply the adapter to the spinner
        spTopic.adapter = topicAdapter
        spTopic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                chosenTopic = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        spColor.adapter = themeAdapter
        spColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                chosenTheme = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun findAllViewsById(view: View) {
        etEmail = view.findViewById(R.id.etEmail)
        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        setUpSpinners(view)

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

        tvLogin.setOnClickListener({
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        })

        btnRegister.setOnClickListener({
            mViewModel?.registerUser(email = etEmail.text.toString().trim(),
                username = etUsername.text.toString().trim(),
                password = etPassword.text.toString().trim(),
                topic = chosenTopic,
                themeColor = chosenTheme,
                f = myObject)
            // Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        })
    }
}