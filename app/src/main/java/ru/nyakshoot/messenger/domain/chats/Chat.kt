package ru.nyakshoot.messenger.domain.chats

import com.google.firebase.Timestamp
import ru.nyakshoot.messenger.data.chats.local.chats.ChatEntity
import ru.nyakshoot.messenger.domain.chat.Message

data class Chat(
    val id: String,
    val ts: Timestamp,
    val lastMessage: Message?,
    val companion: User
) {

    fun toChatEntity(): ChatEntity = ChatEntity(
        id,
        ts,
        lastMessage?.id,
        companion.id
    )

    fun toMap(): Map<String, Any?> {
        return mapOf(
            ID to id,
            TS to ts,
            LAST_MESSAGE to lastMessage,
        )
    }

    companion object PropertyNames {
        const val ID = "id"
        const val TS = "ts"
        const val LAST_MESSAGE = "last_message"
        const val USERS = "users"
        const val COMPANION_ID = "companion_id"
    }

}


