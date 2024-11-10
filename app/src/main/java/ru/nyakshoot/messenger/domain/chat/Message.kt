package ru.nyakshoot.messenger.domain.chat

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import ru.nyakshoot.messenger.data.chat.local.MessageEntity

data class Message(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("sender_id")
    @set:PropertyName("sender_id")
    var senderId: String = "",

    @get:PropertyName("is_read")
    @set:PropertyName("is_read")
    var isRead: Boolean = true,

    @get:PropertyName("ts")
    @set:PropertyName("ts")
    var ts: Timestamp? = null,

    @get:PropertyName("text")
    @set:PropertyName("text")
    var text: String = ""
){
    fun toEntity(chatId: String): MessageEntity {
        return MessageEntity(
            id,
            senderId,
            isRead,
            ts ?: Timestamp.now(),
            text,
            chatId
        )
    }
}
