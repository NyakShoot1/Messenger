package ru.nyakshoot.messenger.data.chat.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import ru.nyakshoot.messenger.data.chats.local.chats.ChatEntity
import ru.nyakshoot.messenger.domain.chat.Message

@Entity(tableName = "message",
    foreignKeys = [ForeignKey(
        entity = ChatEntity::class,
        parentColumns = ["chat_id"],
        childColumns = ["chat_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index("chat_id")
    ]
)
data class MessageEntity(

    @PrimaryKey
    @ColumnInfo(name = "message_id")
    val id: String,

    @ColumnInfo(name = "sender_id")
    val senderId: String,

    @ColumnInfo(name = "is_read")
    val isRead: Boolean,

    @ColumnInfo(name = "message_ts")
    val ts: Timestamp,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "chat_id")
    val chatId: String

){
    fun toModel(): Message {
        return Message(
            id,
            senderId,
            isRead,
            ts,
            text,
        )
    }
}
