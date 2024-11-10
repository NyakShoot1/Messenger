package ru.nyakshoot.messenger.data.chats.local.user

import ru.nyakshoot.messenger.domain.chats.User
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao
): UserLocalDataSource {

    override suspend fun getUser(userId: String): UserEntity {
       return userDao.getUserById(userId)
    }

    override suspend fun insertAll(users: List<User>) {
        userDao.insertAll(users.map { it.toEntity() })
    }

    override suspend fun insert(user: User) {
        userDao.insert(user.toEntity())
    }
}