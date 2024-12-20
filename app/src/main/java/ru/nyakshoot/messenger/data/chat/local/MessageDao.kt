package ru.nyakshoot.messenger.data.chat.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

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
    suspend fun deleteAllFromChat(chatId: String)

    @Transaction
    @Query("UPDATE message SET is_read = 1 WHERE chat_id = :chatId AND sender_id = :senderId")
    suspend fun readMessages(chatId: String, senderId: String)

    @Transaction
    @Query("DELETE FROM message")
    suspend fun deleteAll()

}