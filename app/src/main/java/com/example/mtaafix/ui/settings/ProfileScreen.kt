package com.example.mtaafix.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mtaafix.ui.auth.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = viewModel()
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Email", style = MaterialTheme.typography.labelMedium)
                    Text(text = authViewModel.currentUserEmail, style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Role", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = if (authViewModel.isAdmin) "Admin" else "Citizen",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Change Password", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm new password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (authViewModel.passwordChangeSuccess) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Password updated successfully!",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            authViewModel.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = message, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.changePassword(newPassword, confirmPassword)
                },
                enabled = !authViewModel.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (authViewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Update Password")
                }
            }
        }
    }
}