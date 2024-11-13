package ru.nyakshoot.messenger.data.chats.remote.chats

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.nyakshoot.messenger.domain.chat.Message
import ru.nyakshoot.messenger.domain.chats.Chat
import ru.nyakshoot.messenger.domain.chats.User
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
                    onUpdate(null)
                    return@addSnapshotListener
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val updatedChats =
                        snapshots?.toObjects(ChatFirestore::class.java)?.map { chatFirestore ->
                            val companion = getUserById(chatFirestore.getCompanionId(currentUserId))
                            chatFirestore.toModel(companion)
                        }
                    onUpdate(updatedChats)
                }
            }
    }

    override suspend fun getUserChats(currentUserId: String): List<Chat> {
        return db.collection("chats")
            .whereArrayContains("users", currentUserId)
            .get()
            .await()
            .toObjects(ChatFirestore::class.java).map { chatFirestore ->
                val companion = getUserById(chatFirestore.getCompanionId(currentUserId))
                chatFirestore.toModel(companion)
            }
    }

    override suspend fun deleteChat(chatId: String) {
        db.collection("chats").document(chatId).delete().await()
    }

    override suspend fun createNewChat(chat: ChatFirestore) {
        db.collection("chats").document(chat.id).set(chat).await()
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
}

