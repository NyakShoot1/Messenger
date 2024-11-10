package ru.nyakshoot.messenger.domain.chat

import com.google.firebase.firestore.PropertyName

data class ChatWithMessages(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("messages")
    @set:PropertyName("messages")
    var messages: List<Message> = emptyList(),
)
