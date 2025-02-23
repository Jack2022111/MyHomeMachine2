package com.example.myhomemachine.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authService = AuthService()

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val userId: String, val email: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    fun login(
        email: String,
        password: String,
        onState: (AuthState) -> Unit
    ) {
        viewModelScope.launch {
            onState(AuthState.Loading)
            when (val result = authService.login(email, password)) {
                is AuthService.AuthResult.Success -> {
                    onState(AuthState.Success(result.userId, result.email))
                }
                is AuthService.AuthResult.Error -> {
                    onState(AuthState.Error(result.message))
                }
            }
        }
    }

    fun signup(
        email: String,
        password: String,
        onState: (AuthState) -> Unit
    ) {
        viewModelScope.launch {
            onState(AuthState.Loading)
            when (val result = authService.signup(email, password)) {
                is AuthService.AuthResult.Success -> {
                    onState(AuthState.Success(result.userId, result.email))
                }
                is AuthService.AuthResult.Error -> {
                    onState(AuthState.Error(result.message))
                }
            }
        }
    }
}