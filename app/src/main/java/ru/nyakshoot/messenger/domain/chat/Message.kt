package ru.nyakshoot.messenger.domain.chat

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import ru.nyakshoot.messenger.data.chat.local.MessageEntity
import ru.nyakshoot.messenger.domain.chat.Message.PropertyNames.ID
import ru.nyakshoot.messenger.domain.chat.Message.PropertyNames.IS_READ
import ru.nyakshoot.messenger.domain.chat.Message.PropertyNames.SENDER_ID
import ru.nyakshoot.messenger.domain.chat.Message.PropertyNames.TEXT
import ru.nyakshoot.messenger.domain.chat.Message.PropertyNames.TS

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
) {
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


    companion object PropertyNames {
        const val ID = "id"
        const val SENDER_ID = "sender_id"
        const val IS_READ = "is_read"
        const val TS = "ts"
        const val TEXT = "text"

        fun mapToModel(map: Map<String, Any>): Message {
            return Message(
                id = map[ID] as String,
                senderId = map[SENDER_ID] as String,
                isRead = map[IS_READ] as Boolean,
                ts = map[TS] as Timestamp,
                text = map[TEXT] as String
            )
        }
    }
}

