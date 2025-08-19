package com.example.constructionmobilemajoko

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth


class EmailAndPassword : AppCompatActivity() {

    companion object {
        private const val TAG = "EmailAndPassword"
    }

    // Firebase Auth instance
    private lateinit var auth: FirebaseAuth

    // UI references
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth
        auth.currentUser?.let {
            goToHome()
            return
        }
        // Bind views
        emailEditText    = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton      = findViewById(R.id.loginButton)
        signupButton     = findViewById(R.id.signupButton)

        loginButton.setOnClickListener {
            val email    = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            when {
                email.isEmpty() -> {
                    emailEditText.error = "Missing email"
                    emailEditText.requestFocus()
                }
                password.isEmpty() -> {
                    passwordEditText.error = "Missing password"
                    passwordEditText.requestFocus()
                }
                else -> signIn(email, password)
            }
        }

        signupButton.setOnClickListener {
            val email    = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            when {
                email.isEmpty() -> {
                    emailEditText.error = "Missing email"
                    emailEditText.requestFocus()
                }
                password.isEmpty() -> {
                    passwordEditText.error = "Missing password"
                    passwordEditText.requestFocus()
                }
                else -> createAccount(email, password)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // If user already signed in, send them to HomeActivity
        auth.currentUser?.let { reload() }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    updateUI(auth.currentUser)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                    updateUI(null)
                }
            }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    updateUI(auth.currentUser)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                    updateUI(null)
                }
            }
    }

    private fun sendEmailVerification() {
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener(this) {
                // Optionally notify user that verification email has been sent
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(
                Intent(this, HomeActivity::class.java)
                    .apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
            )
            finish()
        } else {
            // Already showing toast on failure; you could update UI elements here if needed
        }
    }

    private fun reload() {
        // If you want to refresh user data, call user.reload() here
    }
    private fun goToHome() {
        startActivity(Intent(this, HomeActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        finish()
    }
}