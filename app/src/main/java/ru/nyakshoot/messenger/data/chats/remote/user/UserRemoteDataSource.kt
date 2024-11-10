package ru.nyakshoot.messenger.data.chats.remote.user

import ru.nyakshoot.messenger.domain.auth.AuthUser
import ru.nyakshoot.messenger.domain.chats.User
import ru.nyakshoot.messenger.utils.NetworkResult

interface UserRemoteDataSource {

    suspend fun getUsersWithoutChatWithCurrentUser(currentUser: AuthUser): NetworkResult<List<User>>

}