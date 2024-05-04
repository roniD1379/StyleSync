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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.stylesync.Adapter.OnPostClickListener
import com.application.stylesync.Adapter.PostsRecyclerAdapter
import com.application.stylesync.FirebaseAuthManager
import com.application.stylesync.R
import com.application.stylesync.Post
import com.application.stylesync.PostParcelable
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var fBtnEditProfile: FloatingActionButton
    private lateinit var ivProfileImage: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var btnSignOut: Button
    private lateinit var btnHome: Button
    private lateinit var btnCreateNewPost: Button
    private var adapter: PostsRecyclerAdapter? = null
    private var postsRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        findAllViewsById(view)
        setAllOnClicks(view)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.setPosts() {
            setAdapter(view)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private fun findAllViewsById(view: View) {
        fBtnEditProfile = view.findViewById(R.id.fBtnEditProfile)
        ivProfileImage = view.findViewById(R.id.ivProfileImage)
        tvUsername = view.findViewById(R.id.tvUsername)
        btnSignOut = view.findViewById(R.id.btnSignOut)
        btnHome = view.findViewById(R.id.ibHome)
        btnCreateNewPost = view.findViewById(R.id.ibCreatePost)

        if (FirebaseAuthManager.CURRENT_USER.uri.isNotEmpty()) {
            Picasso.get().load(FirebaseAuthManager.CURRENT_USER.uri).into(ivProfileImage)
        }
        tvUsername.text = FirebaseAuthManager.CURRENT_USER.username

    }

    private fun setAllOnClicks(view: View) {
        fBtnEditProfile.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        btnSignOut.setOnClickListener {
            FirebaseAuthManager().signOut()
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment_to_loginFragment)
        }
        btnHome.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment_to_homeFragment)
        }
        btnCreateNewPost.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment_to_createNewPostFragment)
        }
    }

    private fun setAdapter(view: View) {
        adapter = PostsRecyclerAdapter(viewModel.posts)
        postsRecyclerView = view.findViewById(R.id.rvUser_Posts)
        postsRecyclerView?.adapter = adapter
        postsRecyclerView?.layoutManager = LinearLayoutManager(context)
        adapter?.listener = object : OnPostClickListener {


            override fun onPostClicked(post: Post?) {
                val parcelablePost = PostParcelable(post!!)
                val action = ProfileFragmentDirections.actionProfileFragmentToEditPostFragment(parcelablePost)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }
}