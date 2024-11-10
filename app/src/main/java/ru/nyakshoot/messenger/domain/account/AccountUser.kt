package ru.nyakshoot.messenger.domain.account

import com.google.firebase.Timestamp
import ru.nyakshoot.messenger.domain.chats.Chat

data class AccountUser(
    val id: String,
    val email: String,
    val username: String,
    val photoUri: String,
    val ts: Timestamp,
    val chats: List<Chat>
)
