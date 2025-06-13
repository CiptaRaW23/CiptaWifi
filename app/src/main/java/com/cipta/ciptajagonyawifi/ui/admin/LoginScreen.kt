package com.cipta.ciptajagonyawifi.ui.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading = viewModel.isLoading
    val loginSuccess = viewModel.loginSuccess
    val errorMessage = viewModel.errorMessage
    val userRole = viewModel.userRole

    var animateContent by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    val primaryDarkGreen = Color(0xFF1B5E20)
    val softYellow = Color(0xFFFFEE58)

    LaunchedEffect(true) {
        delay(300)
        animateContent = true
    }

    LaunchedEffect(loginSuccess, userRole) {
        if (loginSuccess && !userRole.isNullOrEmpty()) {
            when (userRole) {
                "admin" -> navController.navigate("admin_dashboard") {
                    popUpTo("login") { inclusive = true }
                }
                "user" -> navController.navigate("user_home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            primaryDarkGreen,
            softYellow.copy(alpha = 0.7f),
            Color.White.copy(alpha = 0.2f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = animateContent,
            enter = slideInVertically { with(density) { -40.dp.roundToPx() } } + fadeIn(initialAlpha = 0.3f)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.98f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Masuk Akun Anda",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = primaryDarkGreen,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon", tint = primaryDarkGreen) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryDarkGreen,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.6f),
                            cursorColor = primaryDarkGreen,
                            focusedLabelColor = primaryDarkGreen,
                            unfocusedLabelColor = Color.Gray.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon", tint = primaryDarkGreen) },
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Default.Visibility
                            else Icons.Default.VisibilityOff

                            val description = if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = description, tint = primaryDarkGreen)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryDarkGreen,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.6f),
                            cursorColor = primaryDarkGreen,
                            focusedLabelColor = primaryDarkGreen,
                            unfocusedLabelColor = Color.Gray.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!errorMessage.isNullOrEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = Color.Red,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.login(email.trim(), password.trim())
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryDarkGreen)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Masuk", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = {
                            navController.navigate("register")
                        }
                    ) {
                        Text(
                            text = "Belum punya akun? Daftar Sekarang",
                            color = primaryDarkGreen,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}