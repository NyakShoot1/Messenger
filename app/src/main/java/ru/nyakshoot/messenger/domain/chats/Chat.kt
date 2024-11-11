package ru.nyakshoot.messenger.domain.chats

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import ru.nyakshoot.messenger.data.chats.local.chats.ChatEntity
import ru.nyakshoot.messenger.domain.chat.Message

data class Chat(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("ts")
    @set:PropertyName("ts")
    var ts: Timestamp? = null,

    @get:PropertyName("last_message")
    @set:PropertyName("last_message")
    var lastMessage: Message? = null,

    @get:PropertyName("users")
    @set:PropertyName("users")
    var users: List<String> = emptyList(),

    @PropertyName("")
    var receiverUser: User = User()
) {

    fun toChatEntity(): ChatEntity = ChatEntity(
        id,
        ts ?: Timestamp.now(),
        lastMessage?.id,
        receiverUser.id
    )

}


