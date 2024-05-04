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
    private var mViewModel: RegisterViewModel? = null
    private var mAuth: FirebaseAuth? = null

    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var spTopic: Spinner
    private lateinit var spColor: Spinner
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView;
    private lateinit var themeAdapter: ArrayAdapter<String>;
    private lateinit var topicAdapter: ArrayAdapter<String>;


    private var chosenTopic: String = "A" // todo: change to the first option
    private var chosenTheme: String = "A" // todo: change to the first option

//    val topicOptions = arrayOf("A", "B", "C")
    val themeOptions = ArrayList(Arrays.asList<String>("A", "B", "C"));

    //private var topicOptionsList = listOf<String>()
    var topicOptions = ArrayList(Arrays.asList<String>());
   // private lateinit var themeOptions: Array<String>

    val retrofit = Retrofit.Builder()
        .baseUrl("https://www.csscolorsapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    val call = apiService.getColors()

    private fun setUpSpinnersValues()
    {
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    runBlocking {
                        val apiResponse = response.body()
                        //println(apiResponse)
                        var response = apiResponse!!.colors.map { it.name } // Extracting the colors list
                        //                    println(topicOptions
                        //val adapter = spinner.adapter as ArrayAdapter<String>

                        val arrayList: ArrayList<String> = ArrayList(response)
                        //topicOptions = arrayList
                        //topicAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_dropdown_item, topicOptions)

                        println(response)
                        topicAdapter.clear();
                        topicAdapter.addAll(arrayList);
                        //themeAdapter.addAll(response)

                    }
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize Firebase Auth

        mAuth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        findAllViewsById(view)
        setAllOnClicks(view)
        setUpSpinnersValues()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }


    private fun setUpSpinners (view: View) {
        spTopic = view.findViewById(R.id.spTopic)
        spColor = view.findViewById(R.id.spColor)

        themeAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, themeOptions)
        topicAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, topicOptions)

        topicAdapter.setNotifyOnChange(true);
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
                //chosenTheme = parent.getItemAtPosition(position).toString()
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