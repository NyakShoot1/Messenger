package ru.nyakshoot.messenger.domain.auth

import kotlinx.coroutines.flow.Flow
import ru.nyakshoot.messenger.data.auth.AuthRepository
import ru.nyakshoot.messenger.domain.auth.AuthManager
import ru.nyakshoot.messenger.utils.NetworkResult
import javax.inject.Inject

class AuthManagerImpl @Inject constructor(
    private val authRepository: AuthRepository
) : AuthManager {
    override fun getCurrentAuthUser(): AuthUser? = authRepository.currentAuthUser


    override fun observeCurrentAuthUser(): Flow<AuthUser?> = authRepository.observeCurrentAuthUser()

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): NetworkResult<AuthUser> =
        authRepository.register(email, username, password)


    override suspend fun logIn(email: String, password: String): NetworkResult<AuthUser> =
        authRepository.logIn(email, password)

    override suspend fun logOut() {
        authRepository.logOut()
    }
}