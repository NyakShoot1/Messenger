package ru.nyakshoot.messenger.data.chats.local.chats

import androidx.room.Embedded
import androidx.room.Relation
import ru.nyakshoot.messenger.data.chat.local.MessageEntity
import ru.nyakshoot.messenger.data.chats.local.user.UserEntity

data class ChatWithMessageAndUserEntity(
    @Embedded val chat: ChatEntity,

    @Relation(
        parentColumn = "last_message_id",
        entityColumn = "message_id"
    )
    val lastMessage: MessageEntity?,

    @Relation(
        parentColumn = "companion_id",
        entityColumn = "user_id"
    )
    val companion: UserEntity
)
