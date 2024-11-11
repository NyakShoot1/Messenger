package ru.nyakshoot.messenger.data.chat.remote

import android.util.Log
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.nyakshoot.messenger.domain.chat.ChatWithMessages
import ru.nyakshoot.messenger.domain.chat.Message
import ru.nyakshoot.messenger.utils.NetworkResult
import ru.nyakshoot.messenger.utils.safeFirestoreCall
import javax.inject.Inject

class MessageRemoteDataSourceImpl @Inject constructor(

) : MessageRemoteDataSource {

    private val db get() = FirebaseFirestore.getInstance()

    override suspend fun observeMessages(chatId: String, onUpdate: (List<Message>?) -> Unit) {
        db.collection("chats")
            .document(chatId)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w("Firestore", "Listen failed", error)
                    onUpdate(null)
                    return@addSnapshotListener
                }

                val chat = snapshots?.toObject(ChatWithMessages::class.java)
                val updatedMessages = chat?.messages ?: emptyList()

                onUpdate(updatedMessages)
            }
    }

    override suspend fun getMessages(chatId: String): NetworkResult<List<Message>?> {
        return safeFirestoreCall {
            val chat = db.collection("chats")
                .document(chatId)
                .get()
                .await()
                .toObject(ChatWithMessages::class.java)

            chat?.messages ?: emptyList()
        }
    }

    override suspend fun deleteMessage(chatId: String, messageId: String) {
        val chatRef = db.collection("chats").document(chatId)

        chatRef.get().addOnSuccessListener { chat ->
            val messages = chat.get("messages") as? List<Map<String, Any>> ?: emptyList()

            Log.d("messages", messages.toString())

            val messageToDelete = messages.find { it["id"] == messageId }

            if (messageToDelete != null) {
                chatRef.update("messages", FieldValue.arrayRemove(messageToDelete))
                    .addOnSuccessListener {
                        Log.d("Firestore", "Message deleted successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error deleting message", e)
                    }
            } else {
                Log.d("Firestore", "Message not found")
            }
        }.addOnFailureListener { e ->
            Log.e("Firestore", "Error fetching chat", e)
        }
    }

    override suspend fun readMessages(chatId: String, senderId: String) {
        val chatRef = db.collection("chats").document(chatId)

        chatRef.get()
            .addOnSuccessListener { snapshot ->
                val chatWithMessages = snapshot.toObject(ChatWithMessages::class.java)
                val lastMessage = chatWithMessages?.messages?.lastOrNull()

                // Обновляем поле last_message.is_read, если последнее сообщение от указанного senderId
                if (lastMessage?.senderId == senderId) {
                    chatRef.update("last_message.is_read", true)
                }

                // Получаем список сообщений и обновляем isRead для сообщений от senderId
                val messages = chatWithMessages?.messages ?: emptyList()
                val updatedMessages = messages.map { message ->
                    if (message.senderId == senderId && !message.isRead) {
                        message.isRead = true
                    }
                    // Преобразуем каждое сообщение в HashMap
                    hashMapOf(
                        "id" to message.id,
                        "text" to message.text,
                        "sender_id" to message.senderId,
                        "is_read" to message.isRead,
                        "ts" to message.ts
                    )
                }

                // Сохраняем обновленный список сообщений в Firestore
                chatRef.update("messages", updatedMessages)
            }
    }


    override suspend fun newMessage(chatId: String, message: Message) {
        val chatRef = db.collection("chats").document(chatId)
        chatRef.update("messages", FieldValue.arrayUnion(message))
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    updateLastMessage(chatId, message)
                }
                Log.d("Firestore", "Message added successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding message", e)
            }
    }

    private suspend fun updateLastMessage(chatId: String, message: Message) {
        db.collection("chats")
            .document(chatId)
            .update("last_message", message)
            .await()
    }
}