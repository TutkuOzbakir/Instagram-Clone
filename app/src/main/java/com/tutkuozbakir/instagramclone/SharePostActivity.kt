package com.tutkuozbakir.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tutkuozbakir.instagramclone.databinding.ActivitySharePostBinding
import com.tutkuozbakir.instagramclone.databinding.ActivitySignInBinding

class SharePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySharePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharePostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}