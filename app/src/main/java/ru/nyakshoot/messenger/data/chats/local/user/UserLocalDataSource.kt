package ru.nyakshoot.messenger.data.chats.local.user

import ru.nyakshoot.messenger.domain.chats.User

interface UserLocalDataSource {

    suspend fun getUser(userId: String): UserEntity

    suspend fun insertAll(users: List<User>)

    suspend fun insert(user: User)

}