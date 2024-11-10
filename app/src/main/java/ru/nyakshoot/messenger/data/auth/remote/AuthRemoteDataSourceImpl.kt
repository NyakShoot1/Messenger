package ru.nyakshoot.messenger.data.auth.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ru.nyakshoot.messenger.domain.auth.AuthUser
import ru.nyakshoot.messenger.domain.auth.LogInError
import ru.nyakshoot.messenger.domain.auth.Validation
import ru.nyakshoot.messenger.utils.NetworkResult
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor() : AuthRemoteDataSource {

    private val firebaseAuth: FirebaseAuth get() = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore get() = FirebaseFirestore.getInstance()

    override val authUser: AuthUser?
        get() = firebaseAuth.currentUser?.let { firebaseUser ->
            val userId = firebaseUser.uid
            val userEmail = firebaseUser.email.let(Validation::validEmail)

            if (userEmail != null) {
                AuthUser(userId, userEmail)
            } else {
                null
            }
        }

    override suspend fun createNewUser(
        email: String,
        username: String,
        password: String
    ): NetworkResult<AuthUser> {
        return try {
            val authResult =
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return NetworkResult.error(RegisterError.UNKNOWN.title)

            val newUser = AuthUser(userId, email, username)

            firestore.collection("users").document(userId).set(newUser.toFirestore())

            NetworkResult.success(newUser)
        } catch (e: FirebaseAuthException) {
            NetworkResult.error(RegisterError.USER_WITH_SUCH_CREDENTIALS_EXISTS.title)
        } catch (e: Exception) {
            NetworkResult.error(RegisterError.UNKNOWN.title)
        }


    }

    override suspend fun logIn(
        email: String,
        password: String,
    ): NetworkResult<AuthUser> {
        return try {
            val authResult =
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return NetworkResult.error(LogInError.UNKNOWN.title)

            NetworkResult.success(AuthUser(userId, email))
        } catch (e: FirebaseAuthException) {
            NetworkResult.error(LogInError.INVALID_USER_CREDENTIALS.title)
        } catch (e: Exception) {
            NetworkResult.error(LogInError.UNKNOWN.title)
        }
    }

    override suspend fun logOut() {
        firebaseAuth.signOut()
    }

}