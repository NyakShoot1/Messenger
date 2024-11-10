package ru.nyakshoot.messenger.presentation.auth.viewmodel

sealed class AuthState {

    data object Empty : AuthState()

    data object Authorizing : AuthState()

    data object Authorized : AuthState()

    sealed class InputError : AuthState() {

        data object Email : InputError()

        data object Password : InputError()

        data object Username : InputError()
    }

    data class AuthError(val logInError: String?) : AuthState()
}