package com.cipta.ciptajagonyawifi.ui.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
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

    val isLoading = viewModel.isLoading
    val loginSuccess = viewModel.loginSuccess
    val errorMessage = viewModel.errorMessage
    val userRole = viewModel.userRole

    var animateContent by remember { mutableStateOf(false) }

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

    val accentColor = Color(0xFF2E7D32)
    val gradient = Brush.verticalGradient(
        listOf(Color(0xFFDFFFD8), Color(0xFFA7F0BA))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = animateContent, enter = fadeIn()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .shadow(12.dp, RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.95f), RoundedCornerShape(24.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Masuk Akun", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        cursorColor = accentColor,
                        focusedLabelColor = accentColor
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        cursorColor = accentColor,
                        focusedLabelColor = accentColor
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (!errorMessage.isNullOrEmpty()) {
                    Text(text = errorMessage, color = Color.Red, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.login(email.trim(), password.trim())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Masuk", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        navController.navigate("register")
                    }
                ) {
                    Text("Belum punya akun? Daftar", color = accentColor)
                }
            }
        }
    }
}
