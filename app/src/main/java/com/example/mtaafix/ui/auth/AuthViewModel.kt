package com.example.mtaafix.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    fun signUp(email: String, password: String, confirmPassword: String) {
        errorMessage = null

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Email and password can't be empty"
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Passwords don't match"
            return
        }

        isLoading = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    isLoggedIn = true
                } else {
                    errorMessage = task.exception?.message ?: "Sign up failed"
                }
            }
    }

    fun login(email: String, password: String) {
        errorMessage = null

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Email and password can't be empty"
            return
        }

        isLoading = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    isLoggedIn = true
                } else {
                    errorMessage = task.exception?.message ?: "Login failed"
                }
            }
    }
}