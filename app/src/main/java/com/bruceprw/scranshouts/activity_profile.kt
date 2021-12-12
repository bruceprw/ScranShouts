package com.bruceprw.scranshouts

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


private var mAuth = FirebaseAuth.getInstance()

class activity_profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)

    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        val thisUser = intent.getParcelableExtra<User>("user")
        val profileImageView = findViewById<ImageView>(R.id.profile_image)
        val profileUserNameView = findViewById<TextView>(R.id.profile_username)
        val profileXPView = findViewById<TextView>(R.id.profile_xp)
        val editProfileButton = findViewById<Button>(R.id.edit_profile_button)
        val myReviewsButton = findViewById<Button>(R.id.profile_reviews_button)
        val logOutButton = findViewById<Button>(R.id.log_out_button)

        Picasso.get().load(thisUser.picUrl).into(profileImageView)

        profileUserNameView.text = thisUser.name
        profileXPView.text = thisUser.xp.toString()

        editProfileButton.setOnClickListener { view ->
            val intent = Intent(this, activity_edit_profile::class.java)
            intent.putExtra("user", thisUser)
            startActivity(intent)
        }

        myReviewsButton.setOnClickListener { view ->
            val intent = Intent(this, activity_user_feed::class.java)
            intent.putExtra("user", thisUser)
            startActivity(intent)
        }

        logOutButton.setOnClickListener { view ->
            mAuth.signOut()
            val intent = Intent(this, activity_welcome::class.java)
            showMessage(view, getString(R.string.sign_out_message))
            startActivity(intent)
        }
    }
}