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
import com.application.stylesync.MainActivity
import com.application.stylesync.Post
import com.application.stylesync.R
import com.squareup.picasso.Picasso

class EditPostFragment : Fragment() {

    private var descriptionTextView: EditText? = null
    private var uploadButton: Button? = null
    private var cancelButton: Button? = null
    private var deleteButton: Button? = null
    private var imageUpload: ImageView? = null
    private var imageUri: Uri = Uri.EMPTY;
    private var progressBar: ProgressBar? = null
    private lateinit var spTopic: Spinner
    private lateinit var spColor: Spinner
    private val topicOptions = arrayOf("Classic", "Artsy", "Elegant", "Vintage")
    private val themeOptions = arrayOf("Red", "Green", "Black", "Blue")
    private lateinit var viewModel: EditPostViewModel
    private val args: EditPostFragmentArgs by navArgs()
    private val post = args.selectedPost

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
        descriptionTextView = view.findViewById(R.id.etDescription)
        descriptionTextView?.setText(post.content)
        uploadButton = view.findViewById(R.id.btnUpload)
        cancelButton = view.findViewById(R.id.btnCancel)
        deleteButton = view.findViewById(R.id.btnDelete)
        progressBar = view.findViewById(R.id.progressBar)
        imageUpload = view.findViewById(R.id.uploadImage)
        imageUpload?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            activityResultLauncher.launch(intent)
        }

        Picasso.get().load(post.imageUri).into(imageUpload)

        setUpSpinners(view)

        uploadButton?.setOnClickListener {
            spTopic.selectedItem.toString()

            progressBar?.visibility = View.VISIBLE
            val newPost = Post(
                descriptionTextView?.text.toString(),
                post.imageUri,
                spTopic.selectedItem.toString(),
                spColor.selectedItem.toString(),
                post.userId,
                post.id)

            viewModel.updatePost(
                imageUri,
                newPost
            ) {
                progressBar?.visibility = View.GONE
                Toast.makeText(context, "Post updated", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(view).navigate(R.id.profileFragment)
            }
        }

        cancelButton?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.profileFragment)
        }

        deleteButton?.setOnClickListener {
            viewModel.deletePost(post.id) {
                Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(view).navigate(R.id.profileFragment)
            }
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

        spColor.setSelection(themeOptions.indexOf(post.color))
        spTopic.setSelection(topicOptions.indexOf(post.topic))
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditPostViewModel::class.java)
    }

    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    uploadButton?.isEnabled = true
                    imageUri = result.data?.data!!
                    Picasso.get().load(imageUri).into(imageUpload)                }
            } else {
                Toast.makeText(MainActivity(), "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }

}