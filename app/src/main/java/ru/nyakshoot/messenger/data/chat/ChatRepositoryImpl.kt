package ru.nyakshoot.messenger.data.chat

import com.google.firebase.Timestamp
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import ru.nyakshoot.messenger.data.auth.AuthRepository
import ru.nyakshoot.messenger.data.chat.local.MessageLocalDataSource
import ru.nyakshoot.messenger.data.chat.remote.MessageRemoteDataSource
import ru.nyakshoot.messenger.data.chats.local.chats.ChatsLocalDataSource
import ru.nyakshoot.messenger.domain.chat.Message
import java.util.UUID
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val messageLocalDataSource: MessageLocalDataSource,
    private val messageRemoteDataSource: MessageRemoteDataSource,
    private val authRepository: AuthRepository
): ChatRepository {

    private val currentAuthUser = authRepository.currentAuthUser

    private var currentChatId: String = ""

    override val currentMessages: List<Message>? = null

    override suspend fun observeMessages(chatId: String): Flow<List<Message>?>  = callbackFlow {
        val localMessages = messageLocalDataSource.getChatMessages(chatId)
        trySend(localMessages)

        val remoteMessages = messageRemoteDataSource.getMessages(chatId).data
        if (!remoteMessages.isNullOrEmpty()){
            messageLocalDataSource.insertAll(chatId, remoteMessages)
            trySend(remoteMessages)
        }

        messageRemoteDataSource.observeMessages(chatId){ updateMessages ->
            if (updateMessages != null){
                launch {
                    messageLocalDataSource.insertAll(chatId, updateMessages)
                    trySend(updateMessages)
                }
            }
        }
        awaitClose {  }
    }

    override suspend fun deleteMessage(chatId: String, messageId: String) {
        messageLocalDataSource.deleteMessage(messageId)
        messageRemoteDataSource.deleteMessage(chatId, messageId)
    }

    override suspend fun createNewMessage(text: String, chatId: String) {
        val newMessage = Message(
            id = UUID.randomUUID().toString(),
            ts = Timestamp.now(),
            isRead = false,
            senderId = currentAuthUser!!.id,
            text = text
        )
        messageLocalDataSource.insert(chatId, newMessage)
        messageRemoteDataSource.newMessage(chatId, newMessage)
    }

    override suspend fun logOut(chatId: String) {
        messageLocalDataSource.deleteAll(chatId)
    }
}