package com.bruceprw.scranshouts

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

class activity_user_feed : AppCompatActivity() {
    private var layoutManager : RecyclerView.LayoutManager? = null
    private var adapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>? = null
    private var database = Firebase.database.reference
    private var mAuth = FirebaseAuth.getInstance()
    private lateinit var thisUser : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_review_feed)
        thisUser = intent.getParcelableExtra<User>("user")


    }

    private fun drawView(reviewsList: MutableList<Review>) {
        var recyclerView = findViewById<RecyclerView>(R.id.user_feed_recycler)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = ReviewAdapter(reviewsList)
        recyclerView.adapter = adapter

    }

    private fun populateListFromFirebase() = runBlocking{
        var reviewsRef = database.child("users").child(thisUser.uid!!).child("reviewsList")
        var reviewsList= mutableListOf<Review>()

        val reviewsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    val review : Review? = child.getValue<Review>()
                    if (review != null) {
                        reviewsList.add(review)
                        Log.d("basicListen", "review title: ${review.title}")
                    }
                }
                drawView(reviewsList)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        reviewsRef.addValueEventListener(reviewsListener)
    }

    override fun onStart() {
        super.onStart()
        populateListFromFirebase()
    }
}