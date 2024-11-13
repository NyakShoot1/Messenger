package ru.nyakshoot.messenger.presentation.chats.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.hilt.ScreenModelFactory
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.nyakshoot.messenger.data.chats.ChatsRepository
import ru.nyakshoot.messenger.domain.auth.AuthManager
import ru.nyakshoot.messenger.domain.chats.Chat
import ru.nyakshoot.messenger.domain.chats.User
import ru.nyakshoot.messenger.presentation.chat.viewmodel.ChatScreenModel

class ChatsScreenModel @AssistedInject constructor(
    private val chatsRepository: ChatsRepository,
    private val authManager: AuthManager
) : ScreenModel {

    @AssistedFactory
    interface Factory: ScreenModelFactory {
        fun create() : ChatsScreenModel
    }

    private val _chatsFlow = MutableStateFlow<List<Chat>?>(null)
    val chatsFlow: StateFlow<List<Chat>?> = _chatsFlow

    private val _users = MutableStateFlow<List<User>?>(null)
    val users: StateFlow<List<User>?> = _users

    private val _selectedChats = MutableStateFlow<List<Chat>>(listOf())
    val selectedChats: StateFlow<List<Chat>> = _selectedChats

    private val _chatsState = MutableLiveData<ChatsState>()
    val chatsState: LiveData<ChatsState> get() = _chatsState

    val currentUser = authManager.getCurrentAuthUser()!!

    init {
        observeChats()
    }

    private fun observeChats() {
        screenModelScope.launch {
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

    fun deleteChats() = screenModelScope.launch {
        chatsRepository.deleteChat(_selectedChats.value.map { it.id })
        _selectedChats.value = emptyList()
        _chatsState.value = ChatsState.ChatsLoaded
    }

    fun isSelected(chat: Chat): Boolean {
        return _selectedChats.value.contains(chat)
    }

    fun selectChat(chat: Chat) {
        val currentSelected = _selectedChats.value.toMutableList()
        if (currentSelected.contains(chat)) {
            currentSelected.remove(chat)
        } else {
            currentSelected.add(chat)
        }
        _selectedChats.value = currentSelected

        _chatsState.value = if (currentSelected.isEmpty()) {
            ChatsState.ChatsLoaded
        } else {
            ChatsState.ChatsSelected
        }
    }

    fun resetSelection() {
        _selectedChats.value = emptyList()
        _chatsState.value = ChatsState.ChatsLoaded
    }

    fun createNewChat(user: User) = screenModelScope.launch {
        try {
            chatsRepository.createNewChat(user)
        } catch (e: Exception) {
            Log.e("CreateChatError", e.toString())
        }
    }

    fun getUsers() = screenModelScope.launch {
        try {
            _users.value = chatsRepository.getUsers()
        } catch (e: Exception) {
            Log.e("GetUsersError", e.toString())
        }
    }

    fun logOut() = screenModelScope.launch {
        authManager.logOut()
        chatsRepository.logOut()
    }

}