package com.application.stylesync.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private var mViewModel: ProfileViewModel? = null
    private lateinit var fBtnEditProfile: FloatingActionButton
    private lateinit var ivProfileImage: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var btnSignOut: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        findAllViewsById(view)
        setAllOnClicks(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun findAllViewsById(view: View) {
        fBtnEditProfile = view.findViewById(R.id.fBtnEditProfile)
        ivProfileImage = view.findViewById(R.id.ivProfileImage)
        tvUsername = view.findViewById(R.id.tvUsername)
        btnSignOut = view.findViewById(R.id.btnSignOut)

        if (!FirebaseAuthManager.CURRENT_USER.uri.isEmpty()) {
            Picasso.get().load(FirebaseAuthManager.CURRENT_USER.uri).into(ivProfileImage)
        }
        tvUsername.text = FirebaseAuthManager.CURRENT_USER.username

    }

    private fun setAllOnClicks(view: View) {
        fBtnEditProfile.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        btnSignOut.setOnClickListener({
            FirebaseAuthManager().signOut()
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment_to_loginFragment)
        })
    }
}