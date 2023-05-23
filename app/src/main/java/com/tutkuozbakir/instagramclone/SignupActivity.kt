package com.tutkuozbakir.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tutkuozbakir.instagramclone.databinding.ActivityMainBinding
import com.tutkuozbakir.instagramclone.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Initialize firebase auth
        auth = Firebase.auth

        binding.buttonSignUp.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser(){
        val email = binding.editTextMail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                //success
                val intent = Intent(this@SignupActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                //failure
                Toast.makeText(this@SignupActivity, it.localizedMessage, Toast.LENGTH_LONG)
            }

        }else{
            Toast.makeText(this@SignupActivity, "Please provide email and password.", Toast.LENGTH_LONG)
        }
    }
}