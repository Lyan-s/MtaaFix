package com.example.mtaafix.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }

    if (authViewModel.isLoggedIn) {
        onLoginSuccess()
    }

    LaunchedEffect(authViewModel.isLoggedIn) {
        if (authViewModel.isLoggedIn) {
            showSuccessMessage = true
            delay(2000)
            showSuccessMessage = false
        }
    }

    LaunchedEffect(authViewModel.errorMessage) {
        if (authViewModel.errorMessage != null) {
            showErrorMessage = true
            delay(3000)
            showErrorMessage = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedStatusBanner(
            visible = showSuccessMessage,
            message = "Logged in successfully!",
            isSuccess = true
        )

        AnimatedStatusBanner(
            visible = showErrorMessage,
            message = authViewModel.errorMessage ?: "",
            isSuccess = false
        )

        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { authViewModel.login(email, password) },
            enabled = !authViewModel.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (authViewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Log In")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onNavigateToSignUp) {
            Text("Don't have an account? Sign up")
        }
    }
}