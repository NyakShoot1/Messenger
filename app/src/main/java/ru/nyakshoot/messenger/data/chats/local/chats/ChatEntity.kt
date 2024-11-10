package ru.nyakshoot.messenger.data.chats.local.chats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import ru.nyakshoot.messenger.data.chat.local.MessageEntity
import ru.nyakshoot.messenger.data.chats.local.user.UserEntity


@Entity(
    tableName = "chat",
    foreignKeys = [ForeignKey(
        entity = MessageEntity::class,
        parentColumns = ["message_id"],
        childColumns = ["last_message_id"],
        onDelete = ForeignKey.SET_NULL
    ), ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["user_id"],
        childColumns = ["receiver_user_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index("last_message_id"),
        Index("receiver_user_id"),
    ]
)
data class ChatEntity(

    @PrimaryKey
    @ColumnInfo(name = "chat_id")
    val id: String,

    @ColumnInfo(name = "chat_ts")
    val ts: Timestamp,

    @ColumnInfo(name = "last_message_id")
    val lastMessageId: String?,

    @ColumnInfo(name = "receiver_user_id")
    val receiverUserId: String
)
