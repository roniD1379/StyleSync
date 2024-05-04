package com.application.stylesync.fragments.create_new_post

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.MainActivity
import com.application.stylesync.R
import com.squareup.picasso.Picasso


class CreateNewPostFragment : Fragment() {
    private var descriptionTextView: EditText? = null
    private var uploadButton: Button? = null
    private var cancelButton: Button? = null
    private var imageUpload: ImageView? = null
    private var imageUri: Uri = Uri.EMPTY;
    private var progressBar: ProgressBar? = null
    private var mViewModel: CreateNewPostViewModel? = null
    private lateinit var spTopic: Spinner
    private lateinit var spColor: Spinner
    private lateinit var ibHome: ImageButton
    private lateinit var ibProfile : ImageButton
    private val topicOptions = arrayOf("Classic", "Artsy", "Elegant", "Vintage")
    private val themeOptions = arrayOf("Red", "Green", "Black", "Blue")
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
        descriptionTextView = view.findViewById(R.id.etDescription)
        uploadButton = view.findViewById(R.id.btnUpload)
        cancelButton = view.findViewById(R.id.btnCancel)
        progressBar = view.findViewById(R.id.progressBar)
        imageUpload = view.findViewById(R.id.uploadImage)
        setUpSpinners(view)
        imageUpload?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            activityResultLauncher.launch(intent)
        }


        uploadButton?.setOnClickListener {
            spTopic.selectedItem.toString()
            if (imageUri == Uri.EMPTY) {
                Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar?.visibility = VISIBLE
            mViewModel?.uploadPost(
                imageUri,
                descriptionTextView?.text.toString(),
                spTopic.selectedItem.toString(),
                spColor.selectedItem.toString()
            ) {
                progressBar?.visibility = View.GONE
                Navigation.findNavController(view).navigate(R.id.homeFragment)
            }
        }

        cancelButton?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.homeFragment)
        }
    }

    private fun setUpSpinners(view: View) {
        spTopic = view.findViewById(R.id.spTopic)
        spColor = view.findViewById(R.id.spColor)

        val topicAdapter =
            ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, topicOptions)
        val themeAdapter =
            ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, themeOptions)

        // Apply the adapter to the spinner
        spTopic.adapter = topicAdapter
        spColor.adapter = themeAdapter
    }

    private fun findAllViewsById(view: View) {
        ibProfile = view.findViewById(R.id.ibProfile)
        ibHome = view.findViewById(R.id.ibHome)
    }

    private fun setAllOnClicks(view: View) {
        ibHome.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_createNewPostFragment_to_homeFragment)
        }
        ibProfile.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_createNewPostFragment_to_profileFragment)
        }
    }



    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    uploadButton?.isEnabled = true
                    imageUri = result.data?.data!!
                    Picasso.get().load(imageUri).into(imageUpload)                }
            } else {
                Toast.makeText(MainActivity(), "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
}