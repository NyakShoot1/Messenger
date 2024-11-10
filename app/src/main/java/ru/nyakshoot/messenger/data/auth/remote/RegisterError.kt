package ru.nyakshoot.messenger.data.auth.remote

enum class RegisterError(val title: String) {
    USER_WITH_SUCH_CREDENTIALS_EXISTS("User with such credentials exist"),
    UNKNOWN("unknown")
}