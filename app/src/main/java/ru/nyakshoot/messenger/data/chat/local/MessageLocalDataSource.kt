package ru.nyakshoot.messenger.data.chat.local

import ru.nyakshoot.messenger.domain.chat.Message

interface MessageLocalDataSource {

    suspend fun getChatMessages(chatId: String) : List<Message>

    suspend fun insertAll(chatId: String, messages: List<Message>)

    suspend fun insert(chatId: String, message: Message)

    suspend fun deleteAll(chatId: String)

    suspend fun readMessages(chatId: String, senderId: String)

    suspend fun deleteMessage(messageId: String)

}