package ru.nyakshoot.messenger.data.chats.remote.chats

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import ru.nyakshoot.messenger.data.chats.local.chats.ChatEntity
import ru.nyakshoot.messenger.domain.chat.Message
import ru.nyakshoot.messenger.domain.chats.Chat
import ru.nyakshoot.messenger.domain.chats.User

data class ChatFirestore(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("last_message")
    @set:PropertyName("last_message")
    var lastMessage: Message? = null,

    @get:PropertyName("ts")
    @set:PropertyName("ts")
    var ts: Timestamp = Timestamp.now(),

    @get:PropertyName("users")
    @set:PropertyName("users")
    var users: List<String> = listOf()
) {
    fun toModel(companion: User): Chat {
        return Chat(
            id,
            ts,
            lastMessage,
            companion
        )
    }

    fun toEntity(companionId: String): ChatEntity = ChatEntity(id, ts, lastMessage?.id, companionId)

    fun getCompanionId(currentUserId: String): String {
        return users.firstOrNull { it != currentUserId }!!
    }
}
