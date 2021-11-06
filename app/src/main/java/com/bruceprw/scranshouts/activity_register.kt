package com.bruceprw.scranshouts

import android.content.Context
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
import com.google.firebase.ktx.Firebase

class activity_register : AppCompatActivity()
{



    private var mAuth = FirebaseAuth.getInstance()


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
// [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
// Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
// If sign in fails, display a message to the user.
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

    companion object {
        private const val TAG = "EmailPassword"
    }
}