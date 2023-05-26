package com.tutkuozbakir.instagramclone.view

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
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.tutkuozbakir.instagramclone.R
import com.tutkuozbakir.instagramclone.databinding.ActivitySharePostBinding
import java.util.UUID

class SharePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySharePostBinding
    private lateinit var auth: FirebaseAuth
    private var selectedImage: Uri? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var firestore: FirebaseFirestore //database
    private lateinit var storage: FirebaseStorage //images

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharePostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        registerLaunchers()
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

    fun selectImage(view: View) {
        if(ContextCompat.checkSelfPermission(this@SharePostActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this@SharePostActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                //show snackbar & request permission
                Snackbar.make(view, "Permission needed for gallery.",Snackbar.LENGTH_INDEFINITE).setAction("Give permission",
                    View.OnClickListener {
                        //request permission
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
            }else{
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //start activity for result
            activityResultLauncher.launch(intentToGallery)
        }
    }


    fun sharePost(view: View) {
        //uinversal unique id
        var uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        //upload to storage
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)

        selectedImage?.let{
            imageReference.putFile(it).addOnSuccessListener {
                //download url -> firestore
                val postImageReference = storage.reference.child("images").child(imageName)
                postImageReference.downloadUrl.addOnSuccessListener {
                    val downloadedUrl = it.toString()

                    val post = hashMapOf("email" to auth.currentUser?.email,
                                        "image" to downloadedUrl,
                                        "description" to binding.editTextDescription.text.toString(),
                                        "date" to Timestamp.now())

                    firestore.collection("Posts").add(post).addOnSuccessListener {
                        finish()
                    }.addOnFailureListener{
                        Toast.makeText(this@SharePostActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener{
                Toast.makeText(this@SharePostActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
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
        val intent = Intent(this@SharePostActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
        return super.onOptionsItemSelected(item)
    }
}