package com.application.stylesync.fragments.edit_profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.application.stylesync.ApiResponse
import com.application.stylesync.ApiService
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.FirebaseAuthManagerInterface
import com.application.stylesync.R
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditProfileFragment : Fragment() {
    private lateinit var mViewModel: EditProfileViewModel
    
    private var currentUri: Uri? = null
    private lateinit var ibProfileImage: ImageButton
    private lateinit var etUsername: EditText
    private lateinit var btnUpload: Button
    private lateinit var btnCancel: Button
    private lateinit var spStyle: Spinner
    private lateinit var spColor: Spinner
    private lateinit var colorAdapter: ArrayAdapter<String>;
    private lateinit var styleAdapter: ArrayAdapter<String>;

    // Spinners
    private val styleOptions = arrayOf("Classic", "Casual", "Elegant", "Vintage", "Athleisure", "Bohemian", "Preppy", "Gothic", "Streetwear", "Minimalist")
    private var colorOptions = ArrayList(listOf<String>())
    private var chosenStyle: String = FirebaseAuthManager.CURRENT_USER.style
    private var chosenColor: String = FirebaseAuthManager.CURRENT_USER.color

    // Rest API
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.csscolorsapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiService = retrofit.create(ApiService::class.java)
    private val call = apiService.getColors()
    
    // Profile image
    var galleryTake: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // If the user choose an image, update in imageView, 
            if (uri != null) Picasso.get().load(uri).into(ibProfileImage)
            currentUri = uri
        }

    // Get image from gallery
    private fun pickGallery() {
        galleryTake.launch("image/*")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        findAllViewsById(view)
        setAllOnClicks(view)
        return view
    }

    private fun findAllViewsById(view: View) {
        btnUpload = view.findViewById(R.id.btnUpload)
        btnCancel = view.findViewById(R.id.btnCancel)
        ibProfileImage = view.findViewById(R.id.ibProfileImage)
        etUsername = view.findViewById(R.id.etUsername)
        etUsername.setText(FirebaseAuthManager.CURRENT_USER.username)

        // If user doesn't have profile image, load default one
        if (!FirebaseAuthManager.CURRENT_USER.uri.isEmpty()) {
            Picasso.get().load(FirebaseAuthManager.CURRENT_USER.uri).into(ibProfileImage)
        }

        setUpSpinners(view)
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


        spStyle.setSelection(styleOptions.indexOf(FirebaseAuthManager.CURRENT_USER.style))

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
                    println(FirebaseAuthManager.CURRENT_USER.color)
                    spColor.setSelection(colorOptions.indexOf(FirebaseAuthManager.CURRENT_USER.color))
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(context, "Network call failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAllOnClicks(view: View) {
        ibProfileImage.setOnClickListener {
            pickGallery()
        }

        btnUpload.setOnClickListener {
            val myObject = object : FirebaseAuthManagerInterface {
                override fun success() {
                    Toast.makeText(
                        view.context,
                        "The data updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    currentUri = null
                }

                override fun failure(message: String) {
                    Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
                }
            }

            // Update user data without image
            if (currentUri == null) {
                mViewModel?.updateUser(
                    etUsername.text.toString(),
                    chosenStyle,
                    chosenColor,
                    myObject
                )
            } else { // Update user data with image
                mViewModel?.updateUser(
                    currentUri!!,
                    etUsername.text.toString(),
                    chosenStyle,
                    chosenColor,
                    myObject
                )
            }
        }

        btnCancel.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.profileFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
    }


}