package ru.nyakshoot.messenger.data.chat.remote

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.nyakshoot.messenger.data.chats.remote.chats.ChatFirestore
import ru.nyakshoot.messenger.domain.chat.Message
import javax.inject.Inject

class MessageRemoteDataSourceImpl @Inject constructor(

) : MessageRemoteDataSource {

    private val db get() = FirebaseFirestore.getInstance()

    override suspend fun observeMessages(chatId: String, onUpdate: (List<Message>?) -> Unit) {
        db.collection("messages")
            .document(chatId)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w("Firestore", "Listen failed", error)
                    onUpdate(null)
                    return@addSnapshotListener
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val updatedMessages = snapshots?.getMessagesFromDocument()
                    onUpdate(updatedMessages)
                }
            }
    }

    override suspend fun getMessages(chatId: String): List<Message>? {
        return withContext(Dispatchers.IO) {
            val messagesSnapshot = db.collection("messages")
                .document(chatId)
                .get()
                .await()
            messagesSnapshot?.getMessagesFromDocument()
        }
    }

    override suspend fun deleteMessage(chatId: String, messageId: String) {
        // todo
        val chatRef = db.collection("chats")
            .document(chatId)

        chatRef.get().addOnSuccessListener { chat ->
            val messages = chat.get("messages") as? List<Map<String, Any>> ?: emptyList()

            val messageToDelete = messages.find { it["id"] == messageId }

            if (messageToDelete != null) {
                chatRef.update("messages", FieldValue.arrayRemove(messageToDelete))
            }
        }
    }

    override suspend fun readMessages(chatId: String, senderId: String) {
        val chatRef = db.collection("chats").document(chatId)
        val lastMessage = chatRef.get().await().toObject(ChatFirestore::class.java)?.lastMessage

        if (lastMessage?.senderId == senderId)
            lastMessage.isRead = true

        chatRef.update("last_message", lastMessage)

        val messagesRef = db.collection("messages")
            .document(chatId)

        messagesRef.get()
            .addOnSuccessListener { snapshot ->
                val messages = snapshot?.getMessagesFromDocument()
                messages?.forEach { message ->
                    if (message.senderId == senderId)
                        message.isRead = true
                    messagesRef.update(message.id, message)
                }
            }
    }


    override suspend fun newMessage(chatId: String, message: Message) {
        updateLastMessage(chatId, message)

        db.collection("messages")
            .document(chatId)
            .set(mapOf(message.id to message), SetOptions.merge())
            .await()

    }

    override suspend fun editMessage(chatId: String, message: Message) {
        // todo
    }

    private suspend fun updateLastMessage(chatId: String, message: Message) {
        db.collection("chats")
            .document(chatId)
            .update("last_message", message)
            .await()
    }

    private fun DocumentSnapshot.getMessagesFromDocument(): List<Message> {
        val messages = this.data as? Map<String, Map<String, Any>> ?: emptyMap()

        val listMessages: List<Message> = messages.map { (id, attributes) ->
            Message(
                id = id,
                senderId = attributes[Message.SENDER_ID] as String,
                isRead = attributes[Message.IS_READ] as Boolean,
                ts = attributes[Message.TS] as Timestamp,
                text = attributes[Message.TEXT] as String
            )
        }
        return listMessages.sortedBy { it.ts }
    }
}