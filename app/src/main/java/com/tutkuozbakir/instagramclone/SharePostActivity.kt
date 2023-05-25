package com.tutkuozbakir.instagramclone

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tutkuozbakir.instagramclone.databinding.ActivitySharePostBinding

class SharePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySharePostBinding
    private lateinit var auth: FirebaseAuth
    private var selectedImage: Uri? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharePostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        registerLaunchers()

        binding.imageView.setOnClickListener {
            selectImage()
        }

        binding.buttonSharePost.setOnClickListener {
            sharePost()
        }

    }


    private fun registerLaunchers() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val imageIntent = result.data
                imageIntent?.let{
                    selectedImage = imageIntent.data
                    selectedImage?.let{
                        binding.imageView.setImageURI(it)
                    }
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if(result){
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                Toast.makeText(this@SharePostActivity, "Permission needed for gallery.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun selectImage() {
        if(ContextCompat.checkSelfPermission(this@SharePostActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this@SharePostActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                //show snackbar & request permission
                Snackbar.make(binding.imageView, "Permission needed for gallery.",Snackbar.LENGTH_INDEFINITE).setAction("Give permission",
                    View.OnClickListener {
                        //request permission
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
            }else{
                //resuest permission
            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //start activity for result
            activityResultLauncher.launch(intentToGallery)
        }
    }


    private fun sharePost() {
        TODO("Not yet implemented")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val binding = menuInflater
        binding.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        auth.signOut()
        val intent = Intent(this@SharePostActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
        return super.onOptionsItemSelected(item)
    }
}