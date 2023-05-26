package com.tutkuozbakir.instagramclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tutkuozbakir.instagramclone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        binding.buttonLogIn.setOnClickListener {
            login()
        }
    }

    private fun login(){
        val email = binding.editTextMail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                //success
                val intent = Intent(this@LoginActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                //failure
                Toast.makeText(this@LoginActivity, it.localizedMessage, Toast.LENGTH_LONG)
            }

        }else{
            Toast.makeText(this@LoginActivity, "Please provide email and password.", Toast.LENGTH_LONG)
        }
    }
}