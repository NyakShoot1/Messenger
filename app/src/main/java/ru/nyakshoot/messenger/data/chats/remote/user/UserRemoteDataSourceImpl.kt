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
            // 1. Получаем все чаты текущего пользователя
            val chatsSnapshot = db.collection("chats")
                .whereArrayContains("users", currentUser.id)
                .get()
                .await()

            // 2. Собираем ID всех пользователей из чатов
            val usersWithChats = chatsSnapshot.documents.flatMap { chat ->
                (chat.get("users") as? List<String> ?: emptyList())
            }.toSet()

            // 3. Удаляем ID текущего пользователя из списка
            val userIdsToExclude = usersWithChats.toMutableSet()
            userIdsToExclude.add(currentUser.id) // Добавляем текущего пользователя в исключения

            // 4. Если список пользователей для исключения пустой (кроме текущего пользователя),
            // получаем всех пользователей кроме текущего
            if (userIdsToExclude.size <= 1) {
                return@safeFirestoreCall db.collection("users")
                    .whereNotEqualTo("id", currentUser.id)
                    .get()
                    .await()
                    .toObjects(User::class.java)
            }

            // 5. Получаем пользователей, исключая тех, с кем уже есть чаты
            val usersWithoutChats = db.collection("users")
                .whereNotIn("id", userIdsToExclude.toList())
                .get()
                .await()
                .toObjects(User::class.java)

            usersWithoutChats
        }
    }

}