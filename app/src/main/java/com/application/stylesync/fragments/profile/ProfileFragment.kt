package com.application.stylesync.fragments.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.application.stylesync.R
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File

class ProfileFragment : Fragment() {
    private var mViewModel: ProfileViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    /*
    todo: Example usage of loading image (with error handling)
    fun loadImage(url: String){
    val imageView: ImageView? = view?.findViewById(R.id.imageView)
    Picasso.get().load(url).error(some_default_image).fit().into(imageView)
    }
    */

    fun uploadImage(path: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val file = Uri.fromFile(File(path))

        val uploadTask = storageRef.child("images/${file.lastPathSegment}").putFile(file)

        uploadTask.addOnFailureListener {
            // Handle failure
        }.addOnSuccessListener { taskSnapshot ->
            val storagePath = taskSnapshot.metadata?.path
        }
    }
}