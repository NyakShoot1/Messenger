package ru.nyakshoot.messenger.data.chat.remote

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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

    override suspend fun newMessage(chatId: String, message: Message) {
        val chatRef = db.collection("chats").document(chatId)
        chatRef.update("messages", FieldValue.arrayUnion(message))
            .addOnSuccessListener {
                Log.d("Firestore", "Message added successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding message", e)
            }
    }

    private suspend fun updateLastMessage(){

    }
}