package ru.nyakshoot.messenger.domain.chats

import com.google.firebase.firestore.PropertyName
import ru.nyakshoot.messenger.data.chats.local.user.UserEntity

data class User(
    @get:PropertyName("id")
    val id: String = "",
    @get:PropertyName("username")
    val username: String = "",
){
    fun toEntity(): UserEntity = UserEntity(id = id, username = username)
}