package ru.nyakshoot.messenger.data.chats.local.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nyakshoot.messenger.domain.chats.User

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val id: String,

    @ColumnInfo(name = "email", defaultValue = "")
    val email: String = "",

    @ColumnInfo(name = "username")
    val username: String
){
    fun toModel(): User = User(id, username)
}
