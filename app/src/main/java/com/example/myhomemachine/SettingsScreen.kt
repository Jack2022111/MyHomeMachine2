package com.example.myhomemachine

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myhomemachine.network.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.vector.ImageVector
import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import android.util.Log
import com.example.myhomemachine.data.PersistentDeviceManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    sessionManager: SessionManager,
    deviceManager: PersistentDeviceManager
) {
    val viewModel: AuthViewModel = viewModel()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var currentTheme by remember { mutableStateOf("System Default") }
    var authState by remember { mutableStateOf<AuthViewModel.AuthState>(AuthViewModel.AuthState.Idle) }
    var isLoading by remember { mutableStateOf(false) }

    // Get user info from SessionManager
    val context = LocalContext.current
    val userDetails = sessionManager.getUserDetails()
    val userEmail = userDetails[SessionManager.KEY_USER_EMAIL] ?: ""
    var isRememberMeEnabled by remember { mutableStateOf(sessionManager.isRememberMeEnabled()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Account Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Account",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Email",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = if (userEmail.isNotEmpty()) userEmail else "Not logged in",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Remember Me toggle
                    SettingsToggle(
                        icon = Icons.Default.RememberMe,
                        text = "Remember Me",
                        initialState = isRememberMeEnabled,
                        onToggle = { newValue ->
                            // Update Remember Me preference
                            val userId = userDetails[SessionManager.KEY_USER_ID] ?: "user_id"
                            sessionManager.createLoginSession(userId, userEmail, newValue)
                            isRememberMeEnabled = newValue
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Password Reset Button
                    SettingsButton(
                        icon = Icons.Default.Lock,
                        text = "Reset Password",
                        onClick = {
                            if (userEmail.isNotEmpty()) {
                                viewModel.requestPasswordReset(userEmail) { state ->
                                    authState = state
                                }
                            } else {
                                // Handle case where email is not available
                                navController.navigate("forgot-password")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Delete Account Button
                    SettingsButton(
                        icon = Icons.Default.Delete,
                        text = "Delete Account",
                        onClick = {
                            if (userEmail.isNotEmpty()) {
                                showDeleteAccountDialog = true
                            } else {
                                // Show a snackbar or alert when email isn't available
                                // For now, just log
                                Log.e("SettingsScreen", "Cannot delete account - no email found")
                            }
                        },
                        textColor = MaterialTheme.colorScheme.error,
                        iconTint = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Appearance Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Appearance",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Theme Selection Button
                    SettingsButton(
                        icon = Icons.Default.Palette,
                        text = "Theme",
                        subText = currentTheme,
                        onClick = { showThemeDialog = true }
                    )
                }
            }

            // Notifications Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Push Notifications Toggle
                    SettingsToggle(
                        icon = Icons.Default.Notifications,
                        text = "Push Notifications",
                        initialState = true,
                        onToggle = { /* Handle toggle */ }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Email Notifications Toggle
                    SettingsToggle(
                        icon = Icons.Default.Email,
                        text = "Email Notifications",
                        initialState = false,
                        onToggle = { /* Handle toggle */ }
                    )
                }
            }

            // App Info Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // App Version
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Version",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Version",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "1.0.0",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Logout Button
                    OutlinedButton(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logout")
                    }
                }
            }
        }
    }

    // Theme Dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Select Theme") },
            text = {
                Column {
                    val themes = listOf("System Default", "Light", "Dark")
                    themes.forEach { theme ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    currentTheme = theme
                                    showThemeDialog = false
                                    // Apply theme change logic here
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentTheme == theme,
                                onClick = {
                                    currentTheme = theme
                                    showThemeDialog = false
                                    // Apply theme change logic here
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(theme)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = {
                Text(
                    "Are you sure you want to logout? " +
                            (if (isRememberMeEnabled) "Your account information will be preserved." else "Your account information will not be preserved.")
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Use SessionManager for proper logout
                        sessionManager.logoutUser()
                        showLogoutDialog = false

                        // Navigate to first screen
                        navController.navigate("first") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete Account Confirmation Dialog
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = {
                Text(
                    "Delete Account",
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Column {
                    Text(
                        "Are you sure you want to delete your account? This action cannot be undone and all your data will be permanently deleted.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "If you proceed, a confirmation code will be sent to your email.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    if (isLoading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (userEmail.isNotEmpty()) {
                            isLoading = true
                            Log.d("SettingsScreen", "Requesting account deletion for: $userEmail")
                            viewModel.requestAccountDeletion(userEmail) { state ->
                                isLoading = false
                                authState = state
                                Log.d("SettingsScreen", "Deletion request result: $state")
                                if (state is AuthViewModel.AuthState.Success) {
                                    showDeleteAccountDialog = false

                                    // Just navigate to verification screen - don't log out or clear data yet
                                    navController.navigate("delete-account/${userEmail}")
                                } else if (state is AuthViewModel.AuthState.Error) {
                                    Log.e("SettingsScreen", "Error requesting deletion: ${(state as AuthViewModel.AuthState.Error).message}")
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    enabled = !isLoading
                ) {
                    Text("Yes, Delete Account")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Handle authentication states
    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthState.PasswordResetRequested) {
            // Navigate to the password reset verification screen
            navController.navigate("reset-password-verify/${userEmail}")
            // Reset the state
            authState = AuthViewModel.AuthState.Idle
        }
    }
}

@Composable
fun SettingsButton(
    icon: ImageVector,
    text: String,
    subText: String? = null,
    onClick: () -> Unit,
    textColor: Color = Color.Unspecified,
    iconTint: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconTint
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
            if (subText != null) {
                Text(
                    text = subText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            modifier = Modifier.alpha(0.6f),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SettingsToggle(
    icon: ImageVector,
    text: String,
    initialState: Boolean,
    onToggle: (Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(initialState) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                onToggle(it)
            }
        )
    }
}