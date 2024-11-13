package ru.nyakshoot.messenger.data.chats.remote.chats

import ru.nyakshoot.messenger.domain.chats.Chat
import ru.nyakshoot.messenger.utils.NetworkResult

interface ChatsRemoteDataSource {

    suspend fun observeChats(currentUserId: String, onUpdate: (List<Chat>?) -> Unit)

    suspend fun getUserChats(currentUserId: String): List<Chat>

    suspend fun deleteChat(chatId: String)

    suspend fun createNewChat(chat: ChatFirestore)

}