package com.application.stylesync.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.stylesync.Adapter.PostsRecyclerAdapter
import com.application.stylesync.R

class HomeFragment : Fragment() {
    private lateinit var mViewModel: HomeViewModel
    
    private var adapter: PostsRecyclerAdapter? = null
    private var postsRecyclerView: RecyclerView? = null
    private lateinit var bFilter: Button
    private lateinit var ibClear: ImageButton
    private lateinit var ibProfile: ImageButton
    private lateinit var ibCreatePost : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        findAllViewsById(view)
        setAllOnClicks(view)

        mViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mViewModel.setPosts() {
            setAdapter()
        }

        return view
    }

    private fun setAdapter() {
        adapter = PostsRecyclerAdapter(mViewModel.posts)
        postsRecyclerView = view?.findViewById(R.id.posts_recycler_view)
        postsRecyclerView?.adapter = adapter
        postsRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun findAllViewsById(view: View) {
        bFilter = view.findViewById(R.id.bFilter)
        ibClear = view.findViewById(R.id.ibClear)
        ibProfile = view.findViewById(R.id.ibProfile)
        ibCreatePost = view.findViewById(R.id.ibCreatePost)
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view)
        postsRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun setAllOnClicks(view: View) {
        ibProfile.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_profileFragment)
        }
        ibCreatePost.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_createNewPostFragment)
        }
        bFilter.setOnClickListener {

            val filteredPosts = mViewModel.getFilteredPosts()
            if (filteredPosts.isNotEmpty()) {
                adapter?.setFilter(filteredPosts)
                ibClear.visibility = View.VISIBLE
                Toast.makeText(context, "Filtered posts", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No posts found with your preferences", Toast.LENGTH_SHORT).show()
            }
        }
        ibClear.setOnClickListener {
            ibClear.visibility = View.GONE
            adapter?.setFilter(mViewModel.posts)
            Toast.makeText(context, "Cleared filter", Toast.LENGTH_SHORT).show()
        }
    }
}