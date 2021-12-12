package com.bruceprw.scranshouts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class activity_login : AppCompatActivity()
{
    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val loginButton = findViewById<Button>(R.id.login_button)
        val emailInput = findViewById<EditText>(R.id.login_email)
        val passwordInput = findViewById<EditText>(R.id.login_password)

        loginButton.setOnClickListener { view ->
            login(view, emailInput.text.toString(), passwordInput.text.toString())

        }
    }

    private fun login(view: View, email: String, password: String ) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser!!.displayName
                    closeKeyboard()
                    showMessage(view, getString(R.string.login_success_toast))
                    val intent = Intent(this, activity_main_feed::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                }
                else {
                    closeKeyboard()
                    showMessage(view, getString(R.string.login_failure_toast))

                }
            }


    }
    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}