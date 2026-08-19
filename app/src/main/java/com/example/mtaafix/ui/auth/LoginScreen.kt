package com.example.mtaafix.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mtaafix.R
import kotlinx.coroutines.delay

// ------------------------------------------------------------
// Colors come from MaterialTheme.colorScheme (see ui/theme/Theme.kt).
// This screen is composed under MtaaFixTheme via MainActivity, so
// colorScheme resolves to the MtaaFix brand palette automatically.
// ------------------------------------------------------------

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(true) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }

    val colors = MaterialTheme.colorScheme

    // --------------------------------------------------------
    // Login success & Navigation
    // --------------------------------------------------------
    LaunchedEffect(authViewModel.isLoggedIn) {
        if (authViewModel.isLoggedIn) {
            showSuccessMessage = true
            delay(1500)
            showSuccessMessage = false
            onLoginSuccess()
        }
    }

    // --------------------------------------------------------
    // Login error banner timer
    // --------------------------------------------------------
    LaunchedEffect(authViewModel.errorMessage) {
        if (authViewModel.errorMessage != null) {
            showErrorMessage = true
            delay(4000)
            showErrorMessage = false
        }
    }

    // --------------------------------------------------------
    // Main Screen Layout
    // --------------------------------------------------------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .systemBarsPadding() // FIX 1: Prevents overlapping with the top status bar and bottom navigation bar
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp)) // Added space above the logo

            // Logo
            Image(
                painter = painterResource(id = R.drawable.splash_logo),
                contentDescription = "MtaaFix Logo",
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .height(90.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Header Text
            Text(
                text = "Welcome Back 👋",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onPrimaryContainer,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Let's fix your mtaa.",
                fontSize = 16.sp,
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // FIX 2: Moved Status Banners here so they sit right above the input fields
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

            // Email Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Email",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(7.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter your email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon",
                            tint = colors.primary
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.outline,
                        focusedContainerColor = colors.surfaceVariant,
                        unfocusedContainerColor = colors.surfaceVariant,
                        focusedTextColor = colors.onPrimaryContainer,
                        unfocusedTextColor = colors.onPrimaryContainer,
                        cursorColor = colors.primary
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Password",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(7.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter your password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password Icon",
                            tint = colors.primary
                        )
                    },
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(
                                text = if (passwordVisible) "Hide" else "Show",
                                fontSize = 12.sp,
                                color = colors.onSurfaceVariant
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.outline,
                        focusedContainerColor = colors.surfaceVariant,
                        unfocusedContainerColor = colors.surfaceVariant,
                        focusedTextColor = colors.onPrimaryContainer,
                        unfocusedTextColor = colors.onPrimaryContainer,
                        cursorColor = colors.primary
                    )
                )
            }

            // Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onForgotPassword) {
                    Text(
                        text = "Forgot password?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Login Button
            Button(
                onClick = { authViewModel.login(email, password) },
                enabled = !authViewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    disabledContainerColor = colors.primary.copy(alpha = 0.6f)
                )
            ) {
                if (authViewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = colors.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "LOGIN",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = colors.outline
                )
                Text(
                    text = "  or  ",
                    fontSize = 13.sp,
                    color = colors.onSurfaceVariant
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = colors.outline
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Sign Up Option
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 14.sp,
                    color = colors.onSurfaceVariant
                )
                TextButton(
                    onClick = onNavigateToSignUp,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Sign Up",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary
                    )
                }
            }
        }

        // Branding Footer
        Text(
            text = "Report. Track. Resolve.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = colors.onSurfaceVariant
        )
    }
}