package com.bruceprw.scranshouts

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.runBlocking

class activity_load_review : AppCompatActivity() {
    private var mAuth = FirebaseAuth.getInstance()
    private var database = Firebase.database
    private var databaseRef = database.reference
    private var sv = Firebase.database



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.read_review_page)
        val titleView = findViewById<TextView>(R.id.review_display_title)
        val imgView = findViewById<ImageView>(R.id.review_display_image)
        val bodyView = findViewById<TextView>(R.id.review_display_body)
        val ratingView = findViewById<TextView>(R.id.review_display_rating)
        val xpButton = findViewById<Button>(R.id.xp_button)
        val editRevButton = findViewById<FloatingActionButton>(R.id.edit_review_button)
        var xpGiven = false
        val xpView = findViewById<TextView>(R.id.author_xp)
        val thisUser = mAuth.currentUser
        val review : Review = intent.getParcelableExtra("review")

        titleView.text = review.title
        Picasso.get().load(review.picUrl).into(imgView)
        bodyView.text = review.reviewBody
        ratingView.text = review.rating.toString()
        getAuthorXpandName(review.author!!)
        editRevButton.visibility = View.INVISIBLE

        if (thisUser?.displayName != "") {
            if (thisUser?.uid == review.author) {
                editRevButton.visibility = View.VISIBLE
            }
        }


        editRevButton.setOnClickListener { view ->
            val intent = Intent(this, activity_edit_review::class.java)
            intent.putExtra("review", review)
            startActivity(intent)
        }


        xpButton.setOnClickListener { view ->
            if (thisUser?.displayName != "") {
                if (!xpGiven && thisUser?.uid != review.author) {
                    var xpRef = databaseRef.child("users").child(review.author).child("xp")
                    xpRef.setValue(com.google.firebase.database.ServerValue.increment(1))
                    xpGiven = true
                    databaseRef.child("users").child(review.author).child("xp").get().addOnSuccessListener {
                        val xp = it.value as Long
                        xpView.text = xp.toString()
                    }
                }
                else if (xpGiven){
                    showMessage(view, getString(R.string.multiple_xp_message))
                }
                else if (thisUser?.uid == review.author) {
                    showMessage(view, getString(R.string.self_xp_message))
                }
            }
            else {
                showMessage(view, getString(R.string.guest_xp_message))
            }
                }
        }


    private fun getAuthorXpandName(uid: String) = runBlocking{
        var xp : Long = 0
        var name : String
        val xpView = findViewById<TextView>(R.id.author_xp)
        val authorName = findViewById<TextView>(R.id.author_name)

        databaseRef.child("users").child(uid).child("xp").get().addOnSuccessListener {
            xp = it.value as Long
            xpView.text = xp.toString()
        }

        databaseRef.child("users").child(uid).child("name").get().addOnSuccessListener {
            name = it.value as String
            authorName.text = name
        }
    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

}
