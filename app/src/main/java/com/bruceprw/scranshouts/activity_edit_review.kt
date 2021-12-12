package com.bruceprw.scranshouts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.coroutines.*
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.*

private var mAuth = FirebaseAuth.getInstance()
private var database = Firebase.database.reference
private var reviewRoot = database.child("reviews")
private var estRoot = database.child("establishments")
private var userRoot = database.child("users")
private lateinit var selectAdapter : ArrayAdapter<String>
private val storage = Firebase.storage
private lateinit var myImageUri : Uri
private lateinit var rID : String
private lateinit var thisReview: Review
private val storageRef = storage.reference
private val imagesRef = storageRef.child("images")
private var imageChanged = false

class activity_edit_review : AppCompatActivity(){


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                var tick = findViewById<ImageView>(R.id.review_image_ok)
                tick.visibility = View.VISIBLE

                myImageUri = fileUri
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_review_page)
        val postButton = findViewById<Button>(R.id.publish_review_button)
        val cancelButton = findViewById<Button>(R.id.cancel_review_button)

        val reviewTitle = findViewById<EditText>(R.id.review_title_in)
        var restaurantSpinner = findViewById<Spinner>(R.id.restaurant_spinner)
        val reviewBody = findViewById<EditText>(R.id.review_body_in)
        val ratingSlider = findViewById<Slider>(R.id.slider)
        val imageButton = findViewById<Button>(R.id.review_image_button)
        val user = User("admin", "admin", "https://i.imgur.com/wqq2XZU.png", "admin")

        val reviewData = intent.getParcelableExtra<Review>("review")
        thisReview = reviewData

        reviewTitle.setText(reviewData.title)
        reviewBody.setText(reviewData.reviewBody)
        ratingSlider.value = reviewData.rating!!.toFloat()

        getEstablishmentNamesForSpinner(restaurantSpinner)
        myImageUri = Uri.parse("android.resource://com.bruceprw.scranshouts/" + R.drawable.logo_chicken_only ) // default image

        rID = reviewData.rID!!

        cancelButton.setOnClickListener { view ->
            val intent = Intent(this, activity_main_feed::class.java)
            startActivity(intent)
        }

        postButton.setOnClickListener { view ->
            populateReview(reviewTitle.text.toString(), restaurantSpinner.selectedItem.toString(), reviewBody.text.toString(),
                ratingSlider.value.toInt(), view, user )
        }

        imageButton.setOnClickListener { view ->
            imageChanged = true
            handleImage()
        }
    }


    private fun uploadImage(uri: Uri, review: Review, user: String) {
        if (!imageChanged) {
            postReview(review, user)
        }
        else {

            val uploadRef = storageRef.child("images").child(review.rID!!)
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
                    review.picUrl = task.result.toString()
                    postReview(review, user)

                } else {
                    Log.d("IMGUPLOAD", "FAILED")

                }
            }
            uploadTask.addOnSuccessListener { taskSnapshot ->
                Log.d("IMGUPLOAD", "Image uploaded!")
                taskSnapshot.toString()
                uploadRef.downloadUrl.addOnSuccessListener (object : OnSuccessListener<Uri> {
                    override fun onSuccess(uri: Uri?) {
                        review.picUrl = uri.toString()
                        postReview(review, user)
                        Log.d("DOWNLOADURL:", "DownloadURL = ${review.picUrl}")
                    }
                })
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


    private fun getEstablishmentNamesForSpinner(spinner: Spinner) : MutableList<String> = runBlocking{
        val restNameList = mutableListOf<String>()
        restNameList.add(getString(R.string.select_restaurant))
        estRoot.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (estSnapshot in snapshot.children) {
                    val name = estSnapshot.child("name").getValue(String::class.java)
                    if (name != null) {
                        restNameList.add(name)
                    }
                }
                restNameList.add(getString(R.string.other))
                selectAdapter = ArrayAdapter<String>(this@activity_edit_review, android.R.layout.simple_spinner_dropdown_item, restNameList)
                selectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = selectAdapter
                spinner.setSelection(restNameList.indexOf(thisReview.estName))
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                        parent!!.getChildAt(0)
                        val selectedItem = parent.getItemAtPosition(pos).toString()
                        selectAdapter.notifyDataSetChanged()
                        if (selectedItem == getString(R.string.other)) {
                            if (view != null) {
                                showMessage(view, getString(R.string.Other_Est))
                            }
                        }
                        else if (selectedItem == getString(R.string.select_restaurant)) {
                           // showMessage(view!!, getString(R.string.no_selection_message))
                        }
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        //Do nothing
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DatabaseError", error.toString())
            }
        })

        return@runBlocking restNameList
    }

    private fun populateReview(revTitle : String, restName: String, bodyText: String, rating: Int, view: View, user: User) {
        var picUrl = "https://i.imgur.com/wqq2XZU.png" //Default image
        reviewRoot.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {
                showMessage(view, "${getString(R.string.Review_error)} Details: $error" )
            }
        })

        var currentUser = mAuth.currentUser!!.uid

        val thisRid = rID
        if (restName == getString(R.string.other) || restName == getString(R.string.select_restaurant)) {
            showMessage(view, getString(R.string.no_selection_message))
        }
        else if (bodyText.length >300) {
            showMessage(view, getString(R.string.review_length_message))
        }

        else {
            val review = Review(revTitle, picUrl, restName, thisRid, currentUser, rating, bodyText)
            uploadImage(myImageUri, review, currentUser)
        }
    }

    private fun postReview(review: Review, user: String) {
        reviewRoot.child(review.rID!!).setValue(review)
        addReviewToUser(review, user)

    }

    private fun addReviewToUser(review: Review, user: String) {
        val pushRef = userRoot.child(user).child("reviewsList").child(review.rID!!).setValue(review).addOnSuccessListener(object : OnSuccessListener<Any> {
            override fun onSuccess(p0: Any?) {
                var intent = Intent(this@activity_edit_review, activity_main_feed::class.java)
                startActivity(intent)
            }
        })
    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

}