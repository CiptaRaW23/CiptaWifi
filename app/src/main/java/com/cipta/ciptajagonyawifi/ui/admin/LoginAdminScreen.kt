package com.cipta.ciptajagonyawifi.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun LoginAdminScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading = viewModel.isLoading
    val loginSuccess = viewModel.loginSuccess
    val errorMessage = viewModel.errorMessage

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navController.navigate("admin_dashboard") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login Admin", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            enabled = !isLoading
        ) {
            Text("Login")
        }

        errorMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color.Red, fontSize = 14.sp)
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 12.dp))
        }
    }
}
