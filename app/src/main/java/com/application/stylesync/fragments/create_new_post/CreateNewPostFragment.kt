package com.application.stylesync.fragments.create_new_post

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.application.stylesync.ApiResponse
import com.application.stylesync.ApiService
import com.application.stylesync.MainActivity
import com.application.stylesync.R
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateNewPostFragment : Fragment() {
    private lateinit var mViewModel: CreateNewPostViewModel
    private lateinit var descriptionTextView: EditText
    private lateinit var uploadButton: Button
    private lateinit var cancelButton: Button
    private lateinit var imageUpload: ImageView
    private lateinit var spStyle: Spinner
    private lateinit var spColor: Spinner
    private lateinit var colorAdapter: ArrayAdapter<String>;
    private lateinit var styleAdapter: ArrayAdapter<String>;
    private lateinit var progressBar: ProgressBar
    private var imageUri: Uri = Uri.EMPTY;

    // Spinners
    private var styleOptions = arrayOf("Classic", "Casual", "Elegant", "Vintage", "Athleisure", "Bohemian", "Preppy", "Gothic", "Streetwear", "Minimalist")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        mViewModel = ViewModelProvider(this).get(CreateNewPostViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_new_post, container, false)
        setupUi(view)
        return view
    }

    private fun setupUi(view: View) {
        findAllViewsById(view)
        setUpSpinners(view)
        setAllOnClicks(view)
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

    private fun setUpColorSpinnerValues() {
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val colors = apiResponse!!.colors.map { it.name } // Extracting the colors list
                    val arrayList: ArrayList<String> = ArrayList(colors)
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
        descriptionTextView = view.findViewById(R.id.etDescription)
        uploadButton = view.findViewById(R.id.btnUpload)
        cancelButton = view.findViewById(R.id.btnCancel)
        progressBar = view.findViewById(R.id.progressBar)
        imageUpload = view.findViewById(R.id.uploadImage)
    }

    private fun setAllOnClicks(view: View) {
        imageUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            activityResultLauncher.launch(intent)
        }

        uploadButton.setOnClickListener {
            spStyle.selectedItem.toString()
            if (imageUri == Uri.EMPTY) {
                Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = VISIBLE
            mViewModel.uploadPost(
                imageUri,
                descriptionTextView.text.toString(),
                spStyle.selectedItem.toString(),
                spColor.selectedItem.toString()
            ) {
                progressBar.visibility = View.GONE
                Navigation.findNavController(view).navigate(R.id.homeFragment)
            }
        }

        cancelButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.homeFragment)
        }
    }

    
    
    // load image from gallery
    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    uploadButton.isEnabled = true
                    imageUri = result.data?.data!!
                    Picasso.get().load(imageUri).into(imageUpload)
                }
            } else {
                Toast.makeText(MainActivity(), "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
}