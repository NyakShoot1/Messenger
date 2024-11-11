package ru.nyakshoot.messenger.data.chats.remote.user

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ru.nyakshoot.messenger.domain.auth.AuthUser
import ru.nyakshoot.messenger.domain.chats.User
import ru.nyakshoot.messenger.utils.NetworkResult
import ru.nyakshoot.messenger.utils.safeFirestoreCall
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(

): UserRemoteDataSource {

    private val db get() = FirebaseFirestore.getInstance()

    override suspend fun getUsersWithoutChatWithCurrentUser(currentUser: AuthUser): NetworkResult<List<User>> {
        return safeFirestoreCall {
            val chatsSnapshot = db.collection("chats")
                .whereArrayContains("users", currentUser.id)
                .get()
                .await()

            val usersWithChats = chatsSnapshot.documents.flatMap { chat ->
                (chat.get("users") as? List<String> ?: emptyList())
            }.toSet()

            val userIdsToExclude = usersWithChats.toMutableSet()
            userIdsToExclude.add(currentUser.id)

            if (userIdsToExclude.size <= 1) {
                return@safeFirestoreCall db.collection("users")
                    .whereNotEqualTo("id", currentUser.id)
                    .get()
                    .await()
                    .toObjects(User::class.java)
            }

            val usersWithoutChats = db.collection("users")
                .whereNotIn("id", userIdsToExclude.toList())
                .get()
                .await()
                .toObjects(User::class.java)

            usersWithoutChats
        }
    }

}