package ru.nyakshoot.messenger.data.chats

import android.util.Log
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import ru.nyakshoot.messenger.data.auth.AuthRepository
import ru.nyakshoot.messenger.data.chats.local.chats.ChatsLocalDataSource
import ru.nyakshoot.messenger.data.chats.local.user.UserLocalDataSource
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
    private val authRepository: AuthRepository
) : ChatsRepository {

    private val currentAuthUser = authRepository.currentAuthUser

    override val currentChats: List<Chat>? = null

    override suspend fun observeChats(): Flow<List<Chat>?> = callbackFlow {
        val localChats = chatsLocalDataSource.getUserChats()
        trySend(localChats)

        Log.d("Local", localChats.toString())
        val remoteChats = chatsRemoteDataSource.getUserChats(currentAuthUser?.id.orEmpty()).data
        Log.d("Remote", remoteChats.toString())
        if (!remoteChats.isNullOrEmpty()) {
            userLocalDataSource.insertAll(remoteChats.map { it.receiverUser })
            chatsLocalDataSource.insertAllChats(remoteChats)
            trySend(remoteChats)
        }

        chatsRemoteDataSource.observeChats(currentAuthUser?.id.orEmpty()) { updatedChats ->
            Log.d("Remote", updatedChats.toString())
            if (!updatedChats.isNullOrEmpty()) {
                launch {
                    userLocalDataSource.insertAll(updatedChats.map { it.receiverUser })
                    chatsLocalDataSource.insertAllChats(updatedChats)
                    trySend(updatedChats)
                }
            }
        }

        awaitClose { }
    }

    override suspend fun deleteChat(chatId: String) {
        chatsLocalDataSource.deleteChat(chatId)
        chatsRemoteDataSource.deleteChat(chatId)
    }

    override suspend fun getUsers(): List<User> {
        return userRemoteDataSource.getUsersWithoutChatWithCurrentUser(currentAuthUser!!).data
            ?: emptyList()
    }

    override suspend fun createNewChat(user: User) {
        Log.d("New user", user.toString())
        val newChat = Chat(
            id = UUID.randomUUID().toString(),
            ts = Timestamp.now(),
            lastMessage = null,
            users = listOf(currentAuthUser!!.id, user.id),
            receiverUser = user
        )
        userLocalDataSource.insert(user)
        chatsLocalDataSource.insert(newChat)
        chatsRemoteDataSource.createNewChat(newChat)
    }

    override suspend fun logOut() {
        chatsLocalDataSource.logOut()
    }


}