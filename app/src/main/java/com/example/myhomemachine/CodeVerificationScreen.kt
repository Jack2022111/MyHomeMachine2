package com.example.myhomemachine

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myhomemachine.data.PersistentDeviceManager
import com.example.myhomemachine.network.AuthViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeVerificationScreen(
    navController: NavHostController,
    email: String,
    verificationType: VerificationType,
    sessionManager: SessionManager? = null,
    deviceManager: PersistentDeviceManager? = null
) {
    val viewModel: AuthViewModel = viewModel()
    var verificationCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var authState by remember { mutableStateOf<AuthViewModel.AuthState>(AuthViewModel.AuthState.Idle) }
    var resendState by remember { mutableStateOf<AuthViewModel.AuthState>(AuthViewModel.AuthState.Idle) }
    var showResendSuccess by remember { mutableStateOf(false) }

    // Get context for creating managers if they weren't passed in
    val context = LocalContext.current

    // Create local instances if not provided (fallback)
    val localSessionManager = sessionManager ?: remember { SessionManager(context) }
    val localDeviceManager = deviceManager ?: remember { PersistentDeviceManager(context) }

    // Log the email being received
    LaunchedEffect(email) {
        Log.d("CodeVerificationScreen", "Received email: $email")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(verificationType.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Description
            Text(
                text = verificationType.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Email display
                    Text(
                        text = "Code sent to:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = email,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Verification code input
                    OutlinedTextField(
                        value = verificationCode,
                        onValueChange = {
                            // Only allow digits and limit to 6 characters
                            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                                verificationCode = it
                                errorMessage = null
                            }
                        },
                        label = { Text("Verification Code") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Verification Code"
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = errorMessage != null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Error message
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Resend success message
                    if (showResendSuccess) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "New code sent successfully!",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        // Auto-hide the success message after 3 seconds
                        LaunchedEffect(showResendSuccess) {
                            if (showResendSuccess) {
                                delay(3000)
                                showResendSuccess = false
                            }
                        }
                    }

                    // Resend code button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                // Clear any existing error before attempting to resend
                                errorMessage = null

                                when (verificationType) {
                                    VerificationType.EMAIL_VERIFICATION ->
                                        viewModel.resendVerificationCode(email) { state ->
                                            resendState = state
                                        }
                                    VerificationType.PASSWORD_RESET ->
                                        viewModel.requestPasswordReset(email) { state ->
                                            resendState = state
                                        }
                                    VerificationType.ACCOUNT_DELETION ->
                                        viewModel.requestAccountDeletion(email) { state ->
                                            resendState = state
                                        }
                                }
                            },
                            enabled = resendState !is AuthViewModel.AuthState.Loading
                        ) {
                            if (resendState is AuthViewModel.AuthState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = "Resend Code",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Verify button
            Button(
                onClick = {
                    if (verificationCode.length != 6) {
                        errorMessage = "Please enter a valid 6-digit code"
                        return@Button
                    }

                    // Clear any existing error before verification
                    errorMessage = null

                    when (verificationType) {
                        VerificationType.EMAIL_VERIFICATION -> {
                            viewModel.verifyEmailWithCode(email, verificationCode) { state ->
                                authState = state
                            }
                        }
                        VerificationType.PASSWORD_RESET -> {
                            navController.navigate("reset-password-confirm/$email/$verificationCode")
                        }
                        VerificationType.ACCOUNT_DELETION -> {
                            Log.d("CodeVerificationScreen", "Sending deletion confirmation with code: $verificationCode")
                            viewModel.confirmAccountDeletion(email, verificationCode) { state ->
                                authState = state
                                Log.d("CodeVerificationScreen", "Confirmation result: $state")
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = authState !is AuthViewModel.AuthState.Loading && verificationCode.length == 6
            ) {
                when (authState) {
                    is AuthViewModel.AuthState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    else -> {
                        Text("Verify")
                    }
                }
            }
        }
    }

    // Handle authentication states
    LaunchedEffect(authState) {
        when (authState) {
            is AuthViewModel.AuthState.Success -> {
                Log.d("CodeVerificationScreen", "Success state: ${(authState as AuthViewModel.AuthState.Success).message}")
                when (verificationType) {
                    VerificationType.EMAIL_VERIFICATION -> {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                    VerificationType.PASSWORD_RESET -> {
                        // This case is handled by navigation to reset-password-confirm
                    }
                    VerificationType.ACCOUNT_DELETION -> {
                        Log.d("CodeVerificationScreen", "Account deletion confirmed")

                        // Use deleteUserAccount instead of logoutUser
                        localSessionManager.deleteUserAccount()
                        localDeviceManager.clearCurrentUserDevices()
                        Log.d("CodeVerificationScreen", "User data cleared")

                        navController.navigate("first") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
            is AuthViewModel.AuthState.Error -> {
                Log.e("CodeVerificationScreen", "Error state: ${(authState as AuthViewModel.AuthState.Error).message}")
                errorMessage = (authState as AuthViewModel.AuthState.Error).message
            }
            else -> {}
        }
    }

    // Handle resend states separately
    LaunchedEffect(resendState) {
        when (resendState) {
            is AuthViewModel.AuthState.Success,
            is AuthViewModel.AuthState.PasswordResetRequested -> {
                Log.d("CodeVerificationScreen", "Resend success")
                showResendSuccess = true
                errorMessage = null
                resendState = AuthViewModel.AuthState.Idle // Reset the state
            }
            is AuthViewModel.AuthState.Error -> {
                Log.e("CodeVerificationScreen", "Resend error: ${(resendState as AuthViewModel.AuthState.Error).message}")
                errorMessage = (resendState as AuthViewModel.AuthState.Error).message
                resendState = AuthViewModel.AuthState.Idle // Reset the state
            }
            else -> {}
        }
    }
}

enum class VerificationType(val title: String, val description: String) {
    EMAIL_VERIFICATION(
        "Email Verification",
        "Please enter the 6-digit verification code sent to your email to verify your account."
    ),
    PASSWORD_RESET(
        "Password Reset",
        "Enter the 6-digit code sent to your email to reset your password."
    ),
    ACCOUNT_DELETION(
        "Account Deletion",
        "Enter the 6-digit confirmation code sent to your email to delete your account."
    )
}