package ru.nyakshoot.messenger.data.chats.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE user_id = :userId")
    suspend fun getUserById(userId: String): UserEntity

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Transaction
    @Query("DELETE FROM user")
    suspend fun deleteAll()

}