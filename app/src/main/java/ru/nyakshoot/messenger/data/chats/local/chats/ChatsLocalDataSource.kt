package ru.nyakshoot.messenger.data.chats.local.chats

import ru.nyakshoot.messenger.domain.chats.Chat

interface ChatsLocalDataSource {

    suspend fun getUserChats(): List<Chat>

    suspend fun insertAllChats(chats: List<Chat>)

    suspend fun deleteChat(chatId: String)

    suspend fun insert(chat: Chat)

    suspend fun logOut()

}