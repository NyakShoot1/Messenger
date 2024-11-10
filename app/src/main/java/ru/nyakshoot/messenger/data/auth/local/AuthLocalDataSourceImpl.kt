package ru.nyakshoot.messenger.data.auth.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nyakshoot.messenger.domain.auth.AuthUser
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor() : AuthLocalDataSource {

    private val userFlow: MutableStateFlow<AuthUser?> = MutableStateFlow(null)

    override var currentAuthUser: AuthUser?
        get() = userFlow.value
        set(newValue) {
            userFlow.value = newValue
        }

    override fun observeCurrentAuthUser(): Flow<AuthUser?> = userFlow
}