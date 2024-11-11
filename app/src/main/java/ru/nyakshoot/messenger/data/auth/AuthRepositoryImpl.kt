package ru.nyakshoot.messenger.data.auth

import kotlinx.coroutines.flow.Flow
import ru.nyakshoot.messenger.data.auth.local.AuthLocalDataSource
import ru.nyakshoot.messenger.data.auth.local.logOut
import ru.nyakshoot.messenger.data.auth.remote.AuthRemoteDataSource
import ru.nyakshoot.messenger.domain.auth.AuthUser
import ru.nyakshoot.messenger.utils.NetworkResult
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthRepository {

    init {
        authLocalDataSource.currentAuthUser = authRemoteDataSource.authUser
    }

    override val currentAuthUser: AuthUser?
        get() = authLocalDataSource.currentAuthUser

    override fun observeCurrentAuthUser(): Flow<AuthUser?> =
        authLocalDataSource.observeCurrentAuthUser()

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): NetworkResult<AuthUser> {
        val newUserId = authRemoteDataSource.createNewUser(email, username, password)
        authLocalDataSource.currentAuthUser = authRemoteDataSource.authUser
        return newUserId
    }

    override suspend fun logOut() {
        authRemoteDataSource.logOut()
        authLocalDataSource.logOut()
    }

    override suspend fun logIn(email: String, password: String): NetworkResult<AuthUser> {
        val userId = authRemoteDataSource.logIn(email, password)
        authLocalDataSource.currentAuthUser = authRemoteDataSource.authUser
        return userId
    }

}