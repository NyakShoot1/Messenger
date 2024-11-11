package ru.nyakshoot.messenger.data.chat

import kotlinx.coroutines.flow.Flow
import ru.nyakshoot.messenger.domain.chat.Message

interface ChatRepository {

    val currentMessages: List<Message>?

    suspend fun observeMessages(chatId: String): Flow<List<Message>?>

    suspend fun deleteMessage(chatId: String, messageId: String)

    suspend fun createNewMessage(text: String, chatId: String)

    suspend fun readMessages(chatId: String, senderId: String)

}