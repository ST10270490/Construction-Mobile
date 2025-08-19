package com.example.constructionmobilemajoko

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button

class HomeActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge if you need it
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Wire up the logout button
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // 1. Sign out from Firebase
            auth.signOut()

            // 2. Send back to login screen and clear back-stack
            val intent = Intent(this, EmailAndPassword::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}