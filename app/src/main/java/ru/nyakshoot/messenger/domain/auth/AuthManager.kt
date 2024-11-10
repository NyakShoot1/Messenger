package ru.nyakshoot.messenger.domain.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.nyakshoot.messenger.utils.NetworkResult

interface AuthManager {

    fun getCurrentAuthUser(): AuthUser?

    fun observeCurrentAuthUser(): Flow<AuthUser?>

    suspend fun register(
        email: String,
        username: String,
        password: String
    ): NetworkResult<AuthUser>

    suspend fun logIn(
        email: String,
        password: String
    ): NetworkResult<AuthUser>

    suspend fun logOut()

}

fun AuthManager.isAuthorized(): Boolean = this.getCurrentAuthUser().isAuthorized

fun AuthManager.observeIsAuthorized(): Flow<Boolean> =
    this.observeCurrentAuthUser()
        .map { currentUser -> currentUser.isAuthorized }

private val AuthUser?.isAuthorized: Boolean
    get() = (this != null)