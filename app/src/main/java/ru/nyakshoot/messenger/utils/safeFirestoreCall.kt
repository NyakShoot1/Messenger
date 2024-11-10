package ru.nyakshoot.messenger.utils

import kotlinx.coroutines.tasks.await

suspend fun <T> safeFirestoreCall(call: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.success(call())
    } catch (e: Exception) {
        NetworkResult.error(e.message ?: "error")
    }
}
