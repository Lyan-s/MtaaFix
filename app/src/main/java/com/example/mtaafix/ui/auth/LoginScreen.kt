package com.example.mtaafix.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var showSuccessMessage by remember {
        mutableStateOf(false)
    }

    var showErrorMessage by remember {
        mutableStateOf(false)
    }


    // --------------------------------------------------------
    // Login success
    // --------------------------------------------------------

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


    // --------------------------------------------------------
    // Login error
    // --------------------------------------------------------

    LaunchedEffect(authViewModel.errorMessage) {

        if (authViewModel.errorMessage != null) {

            showErrorMessage = true

            delay(3000)

            showErrorMessage = false
        }
    }


    // --------------------------------------------------------
    // Screen
    // --------------------------------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {

        // ----------------------------------------------------
        // Status messages
        // ----------------------------------------------------

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


        // ----------------------------------------------------
        // Top section
        // ----------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(45.dp)
            )


            // ------------------------------------------------
            // MtaaFix logo
            // ------------------------------------------------

            Image(
                painter = painterResource(
                    id = R.drawable.splash_logo
                ),
                contentDescription = "MtaaFix",
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .height(90.dp)
            )


            Spacer(
                modifier = Modifier.height(25.dp)
            )


            // ------------------------------------------------
            // Welcome text
            // ------------------------------------------------

            Text(
                text = "Welcome Back 👋",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MtaaDarkBlue,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Let's fix your mtaa.",
                fontSize = 16.sp,
                color = MtaaGray,
                textAlign = TextAlign.Center
            )


            Spacer(
                modifier = Modifier.height(32.dp)
            )


            // ------------------------------------------------
            // Email
            // ------------------------------------------------

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Email",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MtaaDarkBlue
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Enter your email"
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = MtaaBlue
                        )
                    },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MtaaBlue,
                        unfocusedBorderColor = MtaaBorder,
                        focusedContainerColor = MtaaLightBlue,
                        unfocusedContainerColor = MtaaLightBlue
                    )
                )
            }


            Spacer(
                modifier = Modifier.height(17.dp)
            )


            // ------------------------------------------------
            // Password
            // ------------------------------------------------

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Password",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MtaaDarkBlue
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Enter your password"
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password",
                            tint = MtaaBlue
                        )
                    },
                    trailingIcon = {

                        IconButton(
                            onClick = {
                                passwordVisible = !passwordVisible
                            }
                        ) {

                            Icon(
                                imageVector =
                                    if (passwordVisible)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,

                                contentDescription =
                                    if (passwordVisible)
                                        "Hide password"
                                    else
                                        "Show password",

                                tint = MtaaGray
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation =
                        if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MtaaBlue,
                        unfocusedBorderColor = MtaaBorder,
                        focusedContainerColor = MtaaLightBlue,
                        unfocusedContainerColor = MtaaLightBlue
                    )
                )
            }


            // ------------------------------------------------
            // Forgot password
            // ------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(
                    onClick = onForgotPassword
                ) {

                    Text(
                        text = "Forgot password?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MtaaBlue
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // ------------------------------------------------
            // LOGIN BUTTON
            // ------------------------------------------------

            Button(
                onClick = {
                    authViewModel.login(
                        email,
                        password
                    )
                },

                enabled = !authViewModel.isLoading,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(12.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = MtaaBlue,
                    disabledContainerColor = MtaaBlue.copy(
                        alpha = 0.6f
                    )
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
                        text = "LOGIN",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            // ------------------------------------------------
            // OR divider
            // ------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MtaaBorder
                )

                Text(
                    text = "  or  ",
                    fontSize = 13.sp,
                    color = MtaaGray
                )

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MtaaBorder
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // ------------------------------------------------
            // Sign up
            // ------------------------------------------------

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Don't have an account? ",
                    fontSize = 14.sp,
                    color = MtaaGray
                )

                TextButton(
                    onClick = onNavigateToSignUp,
                    contentPadding = PaddingValues(0.dp)
                ) {

                    Text(
                        text = "Sign Up",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MtaaOrange
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }


        // ----------------------------------------------------
        // Bottom branding
        // ----------------------------------------------------

        Text(
            text = "Report. Track. Resolve.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MtaaGray
        )
    }
}