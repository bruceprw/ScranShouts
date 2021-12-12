package com.bruceprw.scranshouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.coroutines.*
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class activity_sorted_feed : AppCompatActivity() {

    private var layoutManager : RecyclerView.LayoutManager? = null
    private var adapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>? = null
    private var database = Firebase.database.reference
    private var mAuth = FirebaseAuth.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_feed)
        var plusButton = findViewById<ImageButton?>(R.id.navbar_add_button)
        var profileButton = findViewById<ImageButton>(R.id.navbar_profile_button)
        var sortButton = findViewById<ImageButton>(R.id.navbar_sort_button)


        plusButton.setOnClickListener {view ->
            if (mAuth.currentUser?.displayName != "" ){
                val intent = Intent(this, activity_create_review::class.java)
                startActivity(intent)
            }
            else {
                showMessage(view, "You must be signed in to post a review!")
            }
        }

        profileButton.setOnClickListener {view ->
            if (mAuth.currentUser?.displayName != "" ) {
                database.child("users").child(mAuth.currentUser!!.uid).get().addOnSuccessListener {
                    val thisUser = it.getValue<User>()
                    val intent = Intent(this, activity_profile::class.java)
                    intent.putExtra("user", thisUser)
                    startActivity(intent)
                }

            }
            else {
                showMessage(view, "You must be signed in to view your profile!")
            }
        }

        sortButton.setOnClickListener { view ->
            val intent = Intent(this, activity_main_feed::class.java)
            startActivity(intent)
        }
    }

    private fun drawView(reviewsList: MutableList<Review>) {
        var recyclerView = findViewById<RecyclerView>(R.id.main_feed_recycler)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = ReviewAdapter(reviewsList)
        recyclerView.adapter = adapter

    }

    private fun populateListFromFirebase() = runBlocking{
        var reviewsRef = database.child("reviews").orderByChild("rating")
        var reviewsList= mutableListOf<Review>()

        val reviewsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    val review : Review? = child.getValue<Review>()
                    if (review != null) {
                        val rid = child.child("rid").value as String
                        review.rID = rid
                        reviewsList.add(review)
                        Log.d("basicListen", "review title: ${review.title}")
                    }
                }
                drawView(reviewsList)
            }
            override fun onCancelled(error: DatabaseError) {
                //Do nothing
            }
        }
        reviewsRef.addValueEventListener(reviewsListener)
    }

    override fun onStart() {
        super.onStart()
        populateListFromFirebase()
    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}