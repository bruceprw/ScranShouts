package com.bruceprw.scranshouts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.UUID

class activity_register : AppCompatActivity()
{



    private var mAuth = FirebaseAuth.getInstance()
    private var database = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)
        val registerButton = findViewById<Button>(R.id.register_button)
        val emailInput = findViewById<EditText>(R.id.register_email)
        val passwordInput1 = findViewById<EditText>(R.id.register_password)
        val passwordInput2 = findViewById<EditText>(R.id.confirm_password)

        registerButton.setOnClickListener{ view ->
            val email = emailInput.text.toString()
            val pass1 = passwordInput1.text.toString()
            val pass2 = passwordInput2.text.toString()
            if (pass1 == pass2) {
                createAccount(view, email, pass1)
        }
            else {
                Toast.makeText(baseContext, R.string.register_pwd_toast,
                    Toast.LENGTH_SHORT).show()
            }

        }

    }



    public override fun onStart() {
        super.onStart()

    }

    private fun createAccount(view: View, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = mAuth.currentUser!!
                    createUser(email, user.uid)
                    //updateUI(user)
                    val intent = Intent(this, activity_main_feed::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    showMessage(view, getString(R.string.register_failure_toast))
                    updateUI(null)
                }
            }
    }
    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun createUser(email: String, uid: String) {

        val user = constructNewUserObject(uid, email)
        database.child("users").child(uid).setValue(user)

    }

    private fun constructNewUserObject(uid: String, email: String) : User {
        val email = email
        val name = email
        val picURL = "https://imgur.com/a/RBs4IxQ"
        val uid = uid
        val xp = 0
        val reviewList = mutableListOf<String>()
        val user = User(email, name, picURL, uid, xp, reviewList)
        return user
    }


companion object {
    private const val TAG = "RegisterUser"
}
    }


