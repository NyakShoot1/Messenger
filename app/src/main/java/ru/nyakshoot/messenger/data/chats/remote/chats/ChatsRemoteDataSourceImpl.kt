package ru.nyakshoot.messenger.data.chats.remote.chats

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.nyakshoot.messenger.domain.chats.Chat
import ru.nyakshoot.messenger.domain.chats.User
import ru.nyakshoot.messenger.utils.NetworkResult
import ru.nyakshoot.messenger.utils.safeFirestoreCall
import javax.inject.Inject

class ChatsRemoteDataSourceImpl @Inject constructor(
) : ChatsRemoteDataSource {

    private val db get() = FirebaseFirestore.getInstance()

    override suspend fun observeChats(currentUserId: String, onUpdate: (List<Chat>?) -> Unit) {
        db.collection("chats")
            .whereArrayContains("users", currentUserId)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w("Firestore", "Listen failed", error)
                    onUpdate(null)
                    return@addSnapshotListener
                }

                val updatedChats = snapshots?.toObjects(Chat::class.java)

                CoroutineScope(Dispatchers.IO).launch {
                    updatedChats?.forEach { chat ->
                        val receiverUserId = chat.users.filter { it != currentUserId }
                        val receiverUser = getUserById(receiverUserId[receiverUserId.lastIndex])
                        chat.receiverUser = receiverUser
                    }

                    Log.d("test", updatedChats.toString())
                    onUpdate(updatedChats)
                }
            }
    }

    private suspend fun getUserById(userId: String): User {
        return safeFirestoreCall {
            db.collection("users")
                .document(userId)
                .get()
                .await()
                .toObject(User::class.java)
        }.data ?: User()
    }

    override suspend fun getUserChats(currentUserId: String): NetworkResult<List<Chat>> {
        return safeFirestoreCall {
            val chats = db.collection("chats")
                .whereArrayContains("users", currentUserId)
                .get()
                .await()
                .toObjects(Chat::class.java)

            val updatedChats = chats.map { chat ->
                val receiverUserId = chat.users.firstOrNull { it != currentUserId }
                if (receiverUserId != null) {
                    val receiverUser = getUserById(receiverUserId)
                    chat.receiverUser = receiverUser
                }
                chat
            }
            updatedChats
        }
    }

    override suspend fun deleteChat(chatId: String) {
        db.collection("chats").document(chatId).delete().await()
    }

    override suspend fun createNewChat(chat: Chat) {
        Log.d("new_chat", chat.toString())
        db.collection("chats").document(chat.id).set(chat).await()
    }
}