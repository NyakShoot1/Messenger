package ru.nyakshoot.messenger.presentation.chats.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.nyakshoot.messenger.data.chats.ChatsRepository
import ru.nyakshoot.messenger.domain.auth.AuthManager
import ru.nyakshoot.messenger.domain.chats.Chat
import ru.nyakshoot.messenger.domain.chats.User
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val chatsRepository: ChatsRepository,
    private val authManager: AuthManager
) : ViewModel() {

    private val _chatsFlow = MutableStateFlow<List<Chat>?>(null)
    val chatsFlow: StateFlow<List<Chat>?> = _chatsFlow

    private val _users = MutableStateFlow<List<User>?>(null)
    val users: StateFlow<List<User>?> = _users

    private val _chatsState = MutableLiveData<ChatsState>()
    val chatsState: LiveData<ChatsState> get() = _chatsState

    val currentUser = authManager.getCurrentAuthUser()

    init {
        observeChats()
    }

    private fun observeChats() {
        viewModelScope.launch {
            _chatsState.value = ChatsState.ChatsLoading
            delay(1000)
            chatsRepository.observeChats()
                .catch { e ->
                    e.printStackTrace()
                    _chatsFlow.value = emptyList()
                    _chatsState.value = ChatsState.ChatsLoaded
                }
                .collect { chats ->
                    _chatsFlow.value = chats
                    _chatsState.value = ChatsState.ChatsLoaded
                }
        }
    }

    fun createNewChat(user: User) = viewModelScope.launch {
        try {
            chatsRepository.createNewChat(user)
        } catch (e: Exception) {
            Log.e("CreateChatError", e.toString())
        }
    }

    fun getUsers() = viewModelScope.launch {
        try {
            _users.value = chatsRepository.getUsers()
        } catch (e: Exception) {
            Log.e("GetUsersError", e.toString())
        }
    }

    fun logOut() = viewModelScope.launch {
        authManager.logOut()
        chatsRepository.logOut()
    }

}