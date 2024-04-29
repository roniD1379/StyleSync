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
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.FirebaseAuthManagerInterface
import com.application.stylesync.R
import com.squareup.picasso.Picasso

class EditProfileFragment : Fragment() {
    private var mViewModel: EditProfileViewModel? = null
    private var currentUri: Uri? = null
    private lateinit var ibProfileImage: ImageButton
    private lateinit var etUsername: EditText
    private lateinit var btnUpload: Button
    private lateinit var spTopic: Spinner
    private lateinit var spColor: Spinner

    private var chosenTopic: String =
        FirebaseAuthManager.CURRENT_USER.topic
    private var chosenTheme: String =
        FirebaseAuthManager.CURRENT_USER.themeColor

    val topicOptions = arrayOf("A", "B", "C", "D")
    val themeOptions = arrayOf("A", "B", "C", "D")

    // for getting image from gallery
    var galleryTake: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // put image in imageView, if the user chose an image
            if (uri != null) Picasso.get().load(uri).into(ibProfileImage)
            currentUri = uri
        }

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
        ibProfileImage = view.findViewById(R.id.ibProfileImage)
        etUsername = view.findViewById(R.id.etUsername)
        etUsername.setText(FirebaseAuthManager.CURRENT_USER.username)
        if (!FirebaseAuthManager.CURRENT_USER.uri.isEmpty()) {
            Picasso.get().load(FirebaseAuthManager.CURRENT_USER.uri).into(ibProfileImage)
        }
        setUpSpinners(view)
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
        spTopic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenTopic = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        spColor.adapter = themeAdapter
        spColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenTheme = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        spColor.setSelection(themeOptions.indexOf(FirebaseAuthManager.CURRENT_USER.themeColor))
        spTopic.setSelection(topicOptions.indexOf(FirebaseAuthManager.CURRENT_USER.topic))
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
            if (currentUri == null) {
                mViewModel?.updateUser(
                    etUsername.text.toString(),
                    chosenTopic,
                    chosenTheme,
                    myObject
                )
            } else {
                mViewModel?.updateUser(
                    currentUri!!,
                    etUsername.text.toString(),
                    chosenTopic,
                    chosenTheme,
                    myObject
                )
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
    }


}