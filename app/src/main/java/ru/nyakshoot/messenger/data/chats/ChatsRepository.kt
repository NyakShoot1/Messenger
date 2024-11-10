package ru.nyakshoot.messenger.data.chats

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.nyakshoot.messenger.domain.chats.Chat
import ru.nyakshoot.messenger.domain.chats.User

interface ChatsRepository {

    val currentChats: List<Chat>?

    suspend fun observeChats(): Flow<List<Chat>?>

    suspend fun deleteChat(chatId: String)

    suspend fun getUsers(): List<User>

    suspend fun createNewChat(user: User)

    suspend fun logOut()

}