package ru.nyakshoot.messenger.data.chat.remote

import ru.nyakshoot.messenger.domain.chat.Message

interface MessageRemoteDataSource {

    suspend fun observeMessages(chatId: String, onUpdate: (List<Message>?) -> Unit)

    suspend fun getMessages(chatId: String): List<Message>?

    suspend fun deleteMessage(chatId: String, messageId: String)

    suspend fun readMessages(chatId: String, senderId: String)

    suspend fun newMessage(chatId: String, message: Message)

    suspend fun editMessage(chatId: String, message: Message)

}