package ru.nyakshoot.messenger.domain.auth

import com.google.firebase.Timestamp
import java.util.regex.Pattern

data class AuthUser(
    var id: String,
    val email: String,
    val username: String = ""
){
    fun toFirestore(): HashMap<String, Any> {
        val newUser = HashMap<String, Any>()
        newUser["id"] = this.id
        newUser["email"] = this.email
        newUser["username"] = this.username
        newUser["ts"] = Timestamp.now()
        return newUser
    }
}
