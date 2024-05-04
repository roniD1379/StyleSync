package com.application.stylesync.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.stylesync.Adapter.OnPostClickListener
import com.application.stylesync.Adapter.PostsRecyclerAdapter
import com.application.stylesync.R
import com.example.myapp.firestore.Post

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var ibProfile: ImageButton
    private lateinit var ibCreatePost : ImageButton
    private var adapter: PostsRecyclerAdapter? = null
    private var postsRecyclerView: RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        postsRecyclerView = view?.findViewById(R.id.posts_recycler_view)
        postsRecyclerView?.layoutManager = LinearLayoutManager(context)

        viewModel.setPosts() {
            setAdapter()
        }

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        val button = view.findViewById<View>(R.id.ibCreatePost)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createNewPostFragment)
        }

        findAllViewsById(view)
        setAllOnClicks(view)
        return view
    }

    private fun setAdapter() {
        adapter = PostsRecyclerAdapter(viewModel.posts)
        postsRecyclerView = view?.findViewById(R.id.posts_recycler_view)
        postsRecyclerView?.adapter = adapter
        postsRecyclerView?.layoutManager = LinearLayoutManager(context)
        adapter?.listener = object : OnPostClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(context, "Post $position clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onPostClicked(post: Post?) {
                if (post != null) {
                    Toast.makeText(context, "Post ${post.userId} clicked", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun findAllViewsById(view: View) {
        ibProfile = view.findViewById(R.id.ibProfile)
        ibCreatePost = view.findViewById(R.id.ibCreatePost)
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
    }
}