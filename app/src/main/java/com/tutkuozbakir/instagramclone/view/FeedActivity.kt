package com.tutkuozbakir.instagramclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tutkuozbakir.instagramclone.R
import com.tutkuozbakir.instagramclone.adapter.PostAdapter
import com.tutkuozbakir.instagramclone.databinding.ActivityFeedBinding
import com.tutkuozbakir.instagramclone.model.Post

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val postList = ArrayList<Post>()
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestore = Firebase.firestore

        binding.buttonSharePost.setOnClickListener {
            val intent = Intent(this@FeedActivity, SharePostActivity::class.java)
            startActivity(intent)
        }

        getData()
        binding.recyclerView.layoutManager = LinearLayoutManager(this@FeedActivity)
        postAdapter = PostAdapter(postList)
        binding.recyclerView.adapter = postAdapter
    }

    private fun getData(){
        firestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(this@FeedActivity, error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                if(value != null){
                    if(!value.isEmpty){
                        val documents = value.documents
                        postList.clear()
                        for(document in documents){
                            val post = Post(document.get("description") as String, document.get("email") as String, document.get("image") as String)
                            postList.add(post)
                        }

                        postAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val binding = menuInflater
        binding.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        auth.signOut()
        val intent = Intent(this@FeedActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
        return super.onOptionsItemSelected(item)
    }
}