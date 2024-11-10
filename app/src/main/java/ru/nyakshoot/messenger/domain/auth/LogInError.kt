package ru.nyakshoot.messenger.domain.auth

enum class LogInError(val title: String) {
    INVALID_USER_CREDENTIALS("invalid user credentials"), UNKNOWN("unknown")
}
