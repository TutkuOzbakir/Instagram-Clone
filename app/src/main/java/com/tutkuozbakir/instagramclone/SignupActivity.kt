package com.tutkuozbakir.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tutkuozbakir.instagramclone.databinding.ActivityMainBinding
import com.tutkuozbakir.instagramclone.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}