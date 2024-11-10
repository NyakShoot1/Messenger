package ru.nyakshoot.messenger.data.auth

import kotlinx.coroutines.flow.Flow
import ru.nyakshoot.messenger.domain.auth.AuthUser
import ru.nyakshoot.messenger.utils.NetworkResult

interface AuthRepository {

    val currentAuthUser: AuthUser?

    fun observeCurrentAuthUser(): Flow<AuthUser?>

    suspend fun register(
        email: String,
        username: String,
        password: String,
    ): NetworkResult<AuthUser>

    suspend fun logIn(
        email: String,
        password: String
    ): NetworkResult<AuthUser>

    suspend fun logOut()

}