package ru.nyakshoot.messenger.data.chat.local

import ru.nyakshoot.messenger.data.chats.local.chats.ChatsDao
import ru.nyakshoot.messenger.domain.chat.Message
import javax.inject.Inject

class MessageLocalDataSourceImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val chatsDao: ChatsDao
) : MessageLocalDataSource {
    override suspend fun getChatMessages(chatId: String): List<Message> {
        return messageDao.getMessages(chatId).map { it.toModel() }
    }

    override suspend fun insertAll(chatId: String, messages: List<Message>) {
        messageDao.insertAll(messages.map { it.toEntity(chatId) })
        chatsDao.updateChatLastMessageId(chatId, messages.maxByOrNull { it.ts!! }!!.id)
    }

    override suspend fun insert(chatId: String, message: Message) {
        messageDao.insert(message.toEntity(chatId))
        chatsDao.updateChatLastMessageId(chatId, message.id)
    }

    override suspend fun deleteAll(chatId: String) {
        messageDao.deleteMessage(chatId)
    }

    override suspend fun deleteMessage(messageId: String) {
        messageDao.deleteMessage(messageId)
    }
}