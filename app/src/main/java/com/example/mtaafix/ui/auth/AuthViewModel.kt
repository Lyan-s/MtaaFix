package com.example.mtaafix.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    var isAdmin by mutableStateOf(false)
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
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val userDoc = mapOf(
                        "email" to email,
                        "role" to "citizen"
                    )
                    firestore.collection("users").document(uid)
                        .set(userDoc)
                        .addOnCompleteListener {
                            isLoading = false
                            isAdmin = false
                            isLoggedIn = true
                        }
                } else {
                    isLoading = false
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
                if (task.isSuccessful) {
                    fetchUserRole()
                } else {
                    isLoading = false
                    errorMessage = task.exception?.message ?: "Login failed"
                }
            }
    }

    fun logout() {
        auth.signOut()
        isLoggedIn = false
        isAdmin = false
        errorMessage = null
    }

    private fun fetchUserRole() {
        val uid = auth.currentUser?.uid ?: ""
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                isAdmin = document.getString("role") == "admin"
                isLoading = false
                isLoggedIn = true
            }
            .addOnFailureListener {
                // If the role lookup fails, default to citizen access rather than blocking login
                isAdmin = false
                isLoading = false
                isLoggedIn = true
            }
    }
}