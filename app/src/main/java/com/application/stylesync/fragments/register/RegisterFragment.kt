package com.application.stylesync.fragments.register

import android.R.array
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
import com.application.stylesync.ApiResponse
import com.application.stylesync.ApiService
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.FirebaseAuthManagerInterface
import com.application.stylesync.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Arrays


class RegisterFragment : Fragment() {
    private lateinit var mViewModel: RegisterViewModel

    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var spStyle: Spinner
    private lateinit var spColor: Spinner
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView;
    private lateinit var styleAdapter: ArrayAdapter<String>;
    private lateinit var colorAdapter: ArrayAdapter<String>;

    private var mAuth: FirebaseAuth? = null

    // Spinners
    private val styleOptions = arrayOf("Classic", "Casual", "Elegant", "Vintage", "Athleisure", "Bohemian", "Preppy", "Gothic", "Streetwear", "Minimalist")
    private var colorOptions = ArrayList(listOf<String>())
    private var chosenStyle: String = styleOptions[0]
    private var chosenColor: String = ""

    // Rest API
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.csscolorsapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiService = retrofit.create(ApiService::class.java)
    private val call = apiService.getColors()

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

    private fun setUpSpinners(view: View) {
        spStyle = view.findViewById(R.id.spStyle)
        spColor = view.findViewById(R.id.spColor)
        styleAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, styleOptions)
        colorAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, colorOptions)
        colorAdapter.setNotifyOnChange(true);

        // Apply the adapters to the spinners
        spColor.adapter = colorAdapter
        spColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                chosenColor = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        spStyle.adapter = styleAdapter
        spStyle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                chosenStyle = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        setUpColorSpinnerValues()
    }

    private fun setUpColorSpinnerValues()
    {
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    var apiResponse = response.body()
                    var colors = apiResponse!!.colors.map { it.name } // Extracting the colors list
                    var arrayList: ArrayList<String> = ArrayList(colors)
                    colorAdapter.clear();
                    colorAdapter.addAll(arrayList);
                    chosenColor = arrayList[0]
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(context, "Network call failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
                style = chosenStyle,
                color = chosenColor,
                f = myObject)
        })
    }
}