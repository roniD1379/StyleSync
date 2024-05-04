package com.application.stylesync.fragments.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.stylesync.R

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var ibProfile: ImageButton
    private lateinit var ibNotification : ImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        val button = view.findViewById<View>(R.id.ibCreatePost)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createNewPostFragment)
        }
        findAllViewsById(view)
        setAllOnClicks(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun findAllViewsById(view: View) {
        ibProfile = view.findViewById(R.id.ibProfile)
        ibNotification = view.findViewById(R.id.ibNotification)
    }

    private fun setAllOnClicks(view: View) {
        ibProfile.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_profileFragment)
        }
        ibNotification.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_test)
        }
    }
}