package ru.nyakshoot.messenger.data.chats.local.chats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ChatsDao {

    @Transaction
    @Query("SELECT * FROM chat")
    suspend fun getUserChats() : List<ChatWithMessageAndUserEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<ChatEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chat: ChatEntity)

    @Query("DELETE FROM chat WHERE chat_id = :chatId")
    suspend fun deleteChat(chatId: String)

    @Transaction
    @Query("DELETE FROM chat")
    suspend fun deleteAll()

    @Query("UPDATE chat SET last_message_id = :lastMessageId WHERE chat_id = :chatId")
    suspend fun updateChatLastMessageId(chatId: String, lastMessageId: String)

}