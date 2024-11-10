package ru.nyakshoot.messenger.data.auth.local

import kotlinx.coroutines.flow.Flow
import ru.nyakshoot.messenger.domain.auth.AuthUser

interface AuthLocalDataSource {
    var currentAuthUser: AuthUser?

    fun observeCurrentAuthUser(): Flow<AuthUser?>
}

fun AuthLocalDataSource.logOut() {
    this.currentAuthUser = null
}