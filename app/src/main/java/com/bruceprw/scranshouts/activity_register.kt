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
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import org.json.JSONObject

class activity_register : AppCompatActivity()
{



    private var mAuth = FirebaseAuth.getInstance()
    private var database = Firebase.database.reference
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)
        val registerButton = findViewById<Button>(R.id.register_button)
        val emailInput = findViewById<EditText>(R.id.register_email)
        val passwordInput1 = findViewById<EditText>(R.id.register_password)
        val passwordInput2 = findViewById<EditText>(R.id.confirm_password)

        testDBRead()
        registerButton.setOnClickListener{ view ->
            val email = emailInput.text.toString()
            val pass1 = passwordInput1.text.toString()
            val pass2 = passwordInput2.text.toString()
//            createUser(email)
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
                    val user = mAuth.currentUser
                    createUser(email)
                    updateUI(user)
                    val intent = Intent(this, MainActivity::class.java)
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

    private fun createUser(email: String) {
        database.child("users").child("nextUID").child("value").get().addOnSuccessListener {
            val uidLong = it.value as Long
            val uid = uidLong.toInt()
            val uidString = uid.toString()
            val user = constructNewUserJSON(uid, email)
            database.child("users").child(uidString).setValue(user)

        }
    }

    private fun constructNewUserJSON(uid: Int, email: String) : User {
        val email = email
        val name = email
        val picURL = "https://imgur.com/a/RBs4IxQ"
        val uid = uid
        val xp = 0
        val user = User(email, name, picURL, uid, xp)
        val userJSON = gson.toJson(user)
//        val userJSON = JSONObject("{'email' : '$email', 'name' : '$email', 'picURL' : '$picURL', 'uid' = '$uid'," +
//                " 'xp' : '$xp'}")
        database.child("users").child("nextUID").child("value").setValue(uid+1)
        return user

//        "u0001" : {
//            "email" : "test@test.com",
//            "name" : "test@test.com",
//            "picUrl" : "https://image.com/image123",
//            "uid" : 1,
//            "xp" : 1
//        }
    }

    private fun testDBRead() {
        val uid = database.child("users").child("nextUID").child("value").get().addOnSuccessListener {
            Log.i(TAG,"UID for new user: ${it.value}")

        }



    }

    private fun formatJSON() {

    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}