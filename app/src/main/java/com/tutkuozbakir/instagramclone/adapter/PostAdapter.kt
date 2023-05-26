package com.tutkuozbakir.instagramclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tutkuozbakir.instagramclone.databinding.RecyclerRowBinding
import com.tutkuozbakir.instagramclone.model.Post

class PostAdapter(val postList: ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    class PostHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.email.text = postList.get(position).email
        holder.binding.description.text = postList.get(position).description
        Picasso.get().load(postList.get(position).image).into(holder.binding.image)
    }

    override fun getItemCount(): Int {
        return postList.size
    }


}