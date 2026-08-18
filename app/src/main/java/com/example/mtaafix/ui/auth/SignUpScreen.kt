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
import androidx.compose.ui.graphics.Color
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
// MtaaFix Colors
// ------------------------------------------------------------

private val MtaaBlue = Color(0xFF0B5FA5)
private val MtaaDarkBlue = Color(0xFF073B73)
private val MtaaOrange = Color(0xFFFF8A00)
private val MtaaLightBlue = Color(0xFFF5F9FD)
private val MtaaBorder = Color(0xFFD9E0E8)
private val MtaaGray = Color(0xFF6B7280)


@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel = viewModel(),
    onSignUpSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(true) }
    var confirmPasswordVisible by remember { mutableStateOf(true) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }


    // --------------------------------------------------------
    // Sign up success & Navigation
    // --------------------------------------------------------

    LaunchedEffect(authViewModel.isLoggedIn) {
        if (authViewModel.isLoggedIn) {
            showSuccessMessage = true
            delay(1500)
            showSuccessMessage = false
            onSignUpSuccess()
        }
    }


    // --------------------------------------------------------
    // Sign up error
    // --------------------------------------------------------

    LaunchedEffect(authViewModel.errorMessage) {
        if (authViewModel.errorMessage != null) {
            showErrorMessage = true
            delay(3000)
            showErrorMessage = false
        }
    }


    // --------------------------------------------------------
    // Screen Layout
    // --------------------------------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // ----------------------------------------------------
            // Status messages
            // ----------------------------------------------------

            AnimatedStatusBanner(
                visible = showSuccessMessage,
                message = "Account created!",
                isSuccess = true
            )

            AnimatedStatusBanner(
                visible = showErrorMessage,
                message = authViewModel.errorMessage ?: "",
                isSuccess = false
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ----------------------------------------------------
            // Top section
            // ----------------------------------------------------

            Image(
                painter = painterResource(id = R.drawable.splash_logo),
                contentDescription = "MtaaFix",
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .height(90.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Create Account 📝",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MtaaDarkBlue,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Join us and start fixing your mtaa.",
                fontSize = 16.sp,
                color = MtaaGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))


            // ------------------------------------------------
            // Email
            // ------------------------------------------------

            Column(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = "Email",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MtaaDarkBlue
                )

                Spacer(modifier = Modifier.height(7.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Enter your email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = MtaaBlue
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MtaaBlue,
                        unfocusedBorderColor = MtaaBorder,
                        focusedContainerColor = MtaaLightBlue,
                        unfocusedContainerColor = MtaaLightBlue,
                        focusedTextColor = MtaaDarkBlue,
                        unfocusedTextColor = MtaaDarkBlue,
                        cursorColor = MtaaBlue
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            // ------------------------------------------------
            // Password
            // ------------------------------------------------

            Column(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = "Password",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MtaaDarkBlue
                )

                Spacer(modifier = Modifier.height(7.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Enter your password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password",
                            tint = MtaaBlue
                        )
                    },
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(
                                text = if (passwordVisible) "Hide" else "Show",
                                fontSize = 12.sp,
                                color = MtaaGray
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MtaaBlue,
                        unfocusedBorderColor = MtaaBorder,
                        focusedContainerColor = MtaaLightBlue,
                        unfocusedContainerColor = MtaaLightBlue,
                        focusedTextColor = MtaaDarkBlue,
                        unfocusedTextColor = MtaaDarkBlue,
                        cursorColor = MtaaBlue
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            // ------------------------------------------------
            // Confirm Password
            // ------------------------------------------------

            Column(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = "Confirm Password",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MtaaDarkBlue
                )

                Spacer(modifier = Modifier.height(7.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Re-enter your password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Confirm Password",
                            tint = MtaaBlue
                        )
                    },
                    trailingIcon = {
                        TextButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Text(
                                text = if (confirmPasswordVisible) "Hide" else "Show",
                                fontSize = 12.sp,
                                color = MtaaGray
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MtaaBlue,
                        unfocusedBorderColor = MtaaBorder,
                        focusedContainerColor = MtaaLightBlue,
                        unfocusedContainerColor = MtaaLightBlue,
                        focusedTextColor = MtaaDarkBlue,
                        unfocusedTextColor = MtaaDarkBlue,
                        cursorColor = MtaaBlue
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            // ------------------------------------------------
            // SIGN UP BUTTON
            // ------------------------------------------------

            Button(
                onClick = {
                    authViewModel.signUp(email, password, confirmPassword)
                },
                enabled = !authViewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MtaaBlue,
                    disabledContainerColor = MtaaBlue.copy(alpha = 0.6f)
                )
            ) {
                if (authViewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "SIGN UP",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            // ------------------------------------------------
            // OR divider
            // ------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = MtaaBorder)
                Text(text = "  or  ", fontSize = 13.sp, color = MtaaGray)
                HorizontalDivider(modifier = Modifier.weight(1f), color = MtaaBorder)
            }

            Spacer(modifier = Modifier.height(18.dp))


            // ------------------------------------------------
            // Login link
            // ------------------------------------------------

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 14.sp,
                    color = MtaaGray
                )

                TextButton(
                    onClick = onNavigateToLogin,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Log In",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MtaaOrange
                    )
                }
            }
        }


        // ----------------------------------------------------
        // Bottom branding
        // ----------------------------------------------------

        Text(
            text = "Report. Track. Resolve.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MtaaGray
        )
    }
}