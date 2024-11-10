package ru.nyakshoot.messenger.presentation.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nyakshoot.messenger.domain.auth.AuthManager
import ru.nyakshoot.messenger.domain.auth.AuthUser
import ru.nyakshoot.messenger.domain.auth.Validation
import ru.nyakshoot.messenger.utils.NetworkResult
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()

    val authState: LiveData<AuthState> get() = _authState

    fun logIn(email: String, password: String) {
        val validatedEmail = Validation.validEmail(email)
        val validatedPassword = Validation.validPassword(password)

        when {
            (validatedEmail == null) -> {
                _authState.value = AuthState.InputError.Email
            }

            (validatedPassword == null) -> {
                _authState.value = AuthState.InputError.Password
            }

            else -> {
                executeLogInRequest(validatedEmail, validatedPassword)
            }
        }
    }

    fun register(email: String, username: String, password: String) {
        val validatedEmail = Validation.validEmail(email)
        val validatedPassword = Validation.validPassword(password)
        val validatedUsername = Validation.validUsername(username)

        when {
            (validatedEmail == null) -> {
                _authState.value = AuthState.InputError.Email
            }

            (validatedPassword == null) -> {
                _authState.value = AuthState.InputError.Password
            }

            (validatedUsername == null) -> {
                _authState.value = AuthState.InputError.Username
            }

            else -> {
                executeRegisterRequest(validatedEmail, validatedUsername, validatedPassword)
            }
        }
    }

    private fun executeRegisterRequest(email: String, username: String, password: String) {
        _authState.value = AuthState.Authorizing
        viewModelScope.launch {
            val result = authManager.register(email, username, password)
            handleAuthResult(result)
        }
    }

    private fun executeLogInRequest(email: String, password: String) {
        _authState.value = AuthState.Authorizing
        viewModelScope.launch {
            val result = authManager.logIn(email, password)
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: NetworkResult<AuthUser>) {
        _authState.value = when (result.status) {
            NetworkResult.Status.SUCCESS -> {
                AuthState.Authorized
            }
            NetworkResult.Status.ERROR -> AuthState.AuthError(result.message)
        }
    }

    fun resetAuthState() {
        if (_authState.value == AuthState.AuthError(logInError = null) ||
            _authState.value == AuthState.InputError.Email ||
            _authState.value == AuthState.InputError.Password ||
            _authState.value == AuthState.InputError.Username
        ) {
            _authState.value = AuthState.Empty
        }
    }
}