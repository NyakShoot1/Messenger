package ru.nyakshoot.messenger.data.auth.remote

import ru.nyakshoot.messenger.domain.auth.AuthUser
import ru.nyakshoot.messenger.utils.NetworkResult

interface AuthRemoteDataSource {

    val authUser: AuthUser?

    suspend fun createNewUser(
        email: String,
        username: String,
        password: String,
    ): NetworkResult<AuthUser>

    suspend fun logIn(
        email: String,
        password: String,
    ): NetworkResult<AuthUser>

    suspend fun logOut()

}