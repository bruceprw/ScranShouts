package com.bruceprw.scranshouts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

private var databaseRef = Firebase.database.reference
private val storage = Firebase.storage
private val storageRef = storage.reference
private val imagesRef = storageRef.child("images")
private lateinit var myImageUri : Uri
private lateinit var thisUser : User
private var editImage = false


class activity_edit_profile : AppCompatActivity() {


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                var tick = findViewById<ImageView>(R.id.profile_image_ok)
                tick.visibility = View.VISIBLE
                editImage = true
                myImageUri = fileUri
                uploadImage(myImageUri)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_page)
        thisUser = intent.getParcelableExtra<User>("user")
        val editProfilePicButton = findViewById<Button>(R.id.edit_profile_pic_button)
        val saveChangesButton = findViewById<Button>(R.id.save_profile_button)
        val editNameField = findViewById<EditText>(R.id.edit_username_in)
        editNameField.hint = thisUser.name
        var tick = findViewById<ImageView>(R.id.profile_image_ok)
        tick.visibility = View.INVISIBLE

        editProfilePicButton.setOnClickListener { view ->
            handleImage()
        }

        saveChangesButton.setOnClickListener { view ->
            if(editNameField.text.toString().length <= 40) {
                thisUser.name = editNameField.text.toString()
                saveProfile()
            }
            else {
                showMessage(view, getString(R.string.username_length_warning))
            }
        }
    }

    private fun handleImage() {

        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun uploadImage(uri: Uri) {
        val uploadRef = storageRef.child("images").child(thisUser.uid!!)
        val uploadTask = uploadRef.putFile(myImageUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imagesRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                thisUser.picUrl = task.result.toString()
                saveProfile()

            } else {
                Log.d("IMGUPLOAD", "FAILED")

            }
        }
        uploadTask.addOnSuccessListener { taskSnapshot ->
            Log.d("IMGUPLOAD", "Image uploaded!")
            taskSnapshot.toString()
            uploadRef.downloadUrl.addOnSuccessListener (object : OnSuccessListener<Uri> {
                override fun onSuccess(uri: Uri?) {
                    thisUser.picUrl = uri.toString()
                    saveProfile()
                   // Log.d("DOWNLOADURL:", "DownloadURL = ${review.picUrl}")
                }
            })
        }

    }

    private fun saveProfile() {

        databaseRef.child("users").child(thisUser.uid!!).child("name").setValue(thisUser.name)
        databaseRef.child("users").child(thisUser.uid!!).child("picUrl").setValue(thisUser.picUrl).addOnSuccessListener {
            val intent = Intent(this, activity_profile::class.java)
            intent.putExtra("user", thisUser)
            startActivity(intent)
        }

    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}