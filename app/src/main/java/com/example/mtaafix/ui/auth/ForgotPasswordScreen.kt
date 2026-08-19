package com.example.mtaafix.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mtaafix.R

// ------------------------------------------------------------
// Colors come from MaterialTheme.colorScheme (see ui/theme/Theme.kt).
// This screen is composed under MtaaFixTheme via MainActivity, so
// colorScheme resolves to the MtaaFix brand palette automatically.
// ------------------------------------------------------------

@Composable
fun ForgotPasswordScreen(
    authViewModel: AuthViewModel = viewModel(),
    onBackToLogin: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = "MtaaFix",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(70.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (authViewModel.resetEmailSent) {
            Text(
                text = "Check your email 📩",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We've sent a password reset link to $email. Follow the link to set a new password.",
                fontSize = 15.sp,
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = {
                    authViewModel.clearResetState()
                    onBackToLogin()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                )
            ) {
                Text(text = "Back to Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Text(
                text = "Forgot Password?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter the email linked to your account and we'll send you a reset link.",
                fontSize = 15.sp,
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Enter your email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email", tint = colors.primary)
                },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    focusedContainerColor = colors.surfaceVariant,
                    unfocusedContainerColor = colors.surfaceVariant,
                    focusedTextColor = colors.onPrimaryContainer,
                    unfocusedTextColor = colors.onPrimaryContainer,
                    cursorColor = colors.primary,
                    focusedPlaceholderColor = colors.onSurfaceVariant,
                    unfocusedPlaceholderColor = colors.onSurfaceVariant
                )
            )

            authViewModel.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = message, color = colors.error, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { authViewModel.sendPasswordReset(email) },
                enabled = !authViewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                )
            ) {
                if (authViewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = colors.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "Send Reset Link", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onBackToLogin) {
                Text(text = "Back to Login", color = colors.primary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}