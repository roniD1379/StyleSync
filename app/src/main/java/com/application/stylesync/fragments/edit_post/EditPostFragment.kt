package com.application.stylesync.fragments.edit_post

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.navigation.fragment.navArgs
import com.application.stylesync.ApiResponse
import com.application.stylesync.ApiService
import com.application.stylesync.MainActivity
import com.application.stylesync.Post
import com.application.stylesync.PostParcelable
import com.application.stylesync.R
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditPostFragment : Fragment() {

    private lateinit var descriptionTextView: EditText
    private lateinit var updateButton: Button
    private lateinit var cancelButton: Button
    private lateinit var deleteButton: Button
    private lateinit var imageUpload: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var spStyle: Spinner
    private lateinit var spColor: Spinner
    private lateinit var viewModel: EditPostViewModel
    private lateinit var colorAdapter: ArrayAdapter<String>
    private lateinit var styleAdapter: ArrayAdapter<String>

    private var imageUri: Uri = Uri.EMPTY
    private val args: EditPostFragmentArgs by navArgs()

    // Spinners
    private var styleOptions = arrayOf("Classic", "Casual", "Elegant", "Vintage", "Athleisure", "Bohemian", "Preppy", "Gothic", "Streetwear", "Minimalist")
    private var colorOptions = ArrayList(listOf<String>())

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
        val view = inflater.inflate(R.layout.fragment_edit_post, container, false)
        viewModel = ViewModelProvider(this).get(EditPostViewModel::class.java)
        setupUi(view)
        return view
    }

    private fun setupUi(view: View) {
        val post = args.selectedPost

        setUpSpinners(view)
        findAllViewsById(view)
        setAllOnClicks(view, post)

        descriptionTextView.setText(post.content)
        Picasso.get().load(post.imageUri).into(imageUpload)
    }

    private fun findAllViewsById(view: View) {
        descriptionTextView = view.findViewById(R.id.etDescription)
        updateButton = view.findViewById(R.id.btnUpdate)
        cancelButton = view.findViewById(R.id.btnCancel)
        deleteButton = view.findViewById(R.id.btnDelete)
        imageUpload = view.findViewById(R.id.uploadImage)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun setAllOnClicks(view: View, post: PostParcelable) {
        imageUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            activityResultLauncher.launch(intent)
        }

        updateButton.setOnClickListener {
            spStyle.selectedItem.toString()

            progressBar.visibility = View.VISIBLE
            val newPost = Post(
                descriptionTextView.text.toString(),
                post.imageUri,
                spStyle.selectedItem.toString(),
                spColor.selectedItem.toString(),
                post.userId,
                post.id)

            viewModel.updatePost(
                imageUri,
                newPost
            ) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Post updated", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(view).navigate(R.id.profileFragment)
            }
        }

        cancelButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.profileFragment)
        }

        deleteButton.setOnClickListener {
            viewModel.deletePost(post.id) {
                Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(view).navigate(R.id.profileFragment)
            }
        }
    }

    private fun setUpSpinners(view: View) {
        val post = args.selectedPost
        spStyle = view.findViewById(R.id.spStyle)
        spColor = view.findViewById(R.id.spColor)

        styleAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, styleOptions)
        colorAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, colorOptions)
        colorAdapter.setNotifyOnChange(true);

        // Apply the adapters to the spinners
        spColor.adapter = colorAdapter
        spStyle.adapter = styleAdapter
        spStyle.setSelection(styleOptions.indexOf(post.style))

        setUpColorSpinnerValues(post.color)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditPostViewModel::class.java)
    }

    private fun setUpColorSpinnerValues(defaultColor: String) {
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val colors = apiResponse!!.colors.map { it.name } // Extracting the colors list
                    val arrayList: ArrayList<String> = ArrayList(colors)
                    colorAdapter.clear();
                    colorAdapter.addAll(arrayList);
                    spColor.setSelection(colorOptions.indexOf(defaultColor))
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(context, "Network call failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // load image from gallery
    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    updateButton.isEnabled = true
                    imageUri = result.data?.data!!
                    Picasso.get().load(imageUri).into(imageUpload)                }
            } else {
                Toast.makeText(MainActivity(), "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
}