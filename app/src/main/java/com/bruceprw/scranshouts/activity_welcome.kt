package com.bruceprw.scranshouts

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class activity_welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_screen)

        val loginButton = findViewById<Button>(R.id.welcome_login_button)
        val registerButton = findViewById<Button>(R.id.welcome_register_button)
        val guestButton = findViewById<Button>(R.id.welcome_guest_button)

        loginButton.setOnClickListener { view ->
            val intent = Intent(this, activity_login::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener { view ->
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
        }

        guestButton.setOnClickListener { view ->
            val intent = Intent(this, activity_main_feed::class.java)
            intent.putExtra("User", "guest" )
            startActivity(intent)
        }
    }
}