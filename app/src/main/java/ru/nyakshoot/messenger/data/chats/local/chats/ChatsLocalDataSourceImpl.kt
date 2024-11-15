package ru.nyakshoot.messenger.data.chats.local.chats

import ru.nyakshoot.messenger.data.chat.local.MessageDao
import ru.nyakshoot.messenger.data.chats.remote.chats.ChatFirestore
import ru.nyakshoot.messenger.domain.chats.Chat
import javax.inject.Inject

class ChatsLocalDataSourceImpl @Inject constructor(
    private val chatsDao: ChatsDao,
    private val messageDao: MessageDao
) : ChatsLocalDataSource {

    override suspend fun getUserChats(): List<Chat> {
        return chatsDao.getUserChats().map {
            Chat(
                it.chat.id,
                it.chat.ts,
                it.lastMessage?.toModel(),
                companion = it.companion.toModel()
            )
        }
    }

    override suspend fun insertAllChats(chats: List<Chat>) {
        chats.map { if (it.lastMessage != null) messageDao.insert(it.lastMessage.toEntity(it.id)) } // todo перенос в repository
        chatsDao.insertAll(chats.map { it.toChatEntity() })
    }

    override suspend fun deleteChat(chatId: String) {
        chatsDao.deleteChat(chatId)
    }

    override suspend fun insert(chat: ChatEntity) {
        chatsDao.insert(chat)
    }

    override suspend fun logOut() {
        chatsDao.deleteAll()
    }
}