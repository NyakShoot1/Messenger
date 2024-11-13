package ru.nyakshoot.messenger.data.chats

import com.google.firebase.Timestamp
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import ru.nyakshoot.messenger.data.auth.AuthRepository
import ru.nyakshoot.messenger.data.chat.local.MessageLocalDataSource
import ru.nyakshoot.messenger.data.chats.local.chats.ChatsLocalDataSource
import ru.nyakshoot.messenger.data.chats.local.user.UserLocalDataSource
import ru.nyakshoot.messenger.data.chats.remote.chats.ChatFirestore
import ru.nyakshoot.messenger.data.chats.remote.chats.ChatsRemoteDataSource
import ru.nyakshoot.messenger.data.chats.remote.user.UserRemoteDataSource
import ru.nyakshoot.messenger.domain.chats.Chat
import ru.nyakshoot.messenger.domain.chats.User
import java.util.UUID
import javax.inject.Inject

class ChatsRepositoryImpl @Inject constructor(
    private val chatsLocalDataSource: ChatsLocalDataSource,
    private val chatsRemoteDataSource: ChatsRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val messageLocalDataSource: MessageLocalDataSource,
    private val authRepository: AuthRepository
) : ChatsRepository {

    private val currentAuthUser = authRepository.currentAuthUser

    override val currentChats: List<Chat>? = null

    override suspend fun observeChats(): Flow<List<Chat>?> = callbackFlow {
        val localChats = chatsLocalDataSource.getUserChats().sortedBy { it.ts }
        trySend(localChats)

        val remoteChats = chatsRemoteDataSource.getUserChats(currentAuthUser?.id.orEmpty()).sortedBy { it.ts }
        if (remoteChats.isNotEmpty()) {
            launch {
                userLocalDataSource.insertAll(remoteChats.map { it.companion })
                chatsLocalDataSource.insertAllChats(remoteChats)
            }
            trySend(remoteChats)
        }

        chatsRemoteDataSource.observeChats(currentAuthUser?.id.orEmpty()) { updatedChats ->
            if (!updatedChats.isNullOrEmpty()) {
                launch {
                    userLocalDataSource.insertAll(updatedChats.map { it.companion })
                    chatsLocalDataSource.insertAllChats(updatedChats)
                }
                trySend(updatedChats.sortedBy { it.ts })
            }
        }

        awaitClose { }
    }

    override suspend fun deleteChat(chatsId: List<String>) {
        chatsId.forEach {  chatId ->
            messageLocalDataSource.deleteAllFromChat(chatId)
            chatsLocalDataSource.deleteChat(chatId)
            chatsRemoteDataSource.deleteChat(chatId)
        }
    }

    override suspend fun getUsers(): List<User> {
        return userRemoteDataSource.getUsersWithoutChatWithCurrentUser(currentAuthUser!!).data
            ?: emptyList()
    }

    override suspend fun createNewChat(companion: User) {
        val newChat = ChatFirestore(
            id = UUID.randomUUID().toString(),
            ts = Timestamp.now(),
            lastMessage = null,
            users = listOf(currentAuthUser!!.id, companion.id),
        )
        userLocalDataSource.insert(companion)
        chatsLocalDataSource.insert(newChat.toEntity(companion.id))
        chatsRemoteDataSource.createNewChat(newChat)
    }

    override suspend fun logOut() {
        chatsLocalDataSource.logOut()
        userLocalDataSource.logOut()
        messageLocalDataSource.logOut()
    }
}