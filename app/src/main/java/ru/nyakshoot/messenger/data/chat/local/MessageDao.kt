package ru.nyakshoot.messenger.data.chat.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.nyakshoot.messenger.data.chats.local.chats.ChatEntity
import ru.nyakshoot.messenger.domain.chat.Message

@Dao
interface MessageDao {

    @Transaction
    @Query("SELECT * FROM message WHERE chat_id = :chatId")
    suspend fun getMessages(chatId: String) : List<MessageEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageEntity)

    @Query("DELETE FROM message WHERE message_id = :messageId")
    suspend fun deleteMessage(messageId: String)

    @Transaction
    @Query("DELETE FROM message WHERE chat_id = :chatId")
    suspend fun deleteAll(chatId: String)

}