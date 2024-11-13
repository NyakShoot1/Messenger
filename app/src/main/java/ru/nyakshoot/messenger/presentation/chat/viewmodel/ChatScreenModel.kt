package ru.nyakshoot.messenger.presentation.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.hilt.ScreenModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.nyakshoot.messenger.data.auth.AuthRepository
import ru.nyakshoot.messenger.data.chat.ChatRepository
import ru.nyakshoot.messenger.domain.chat.Message
import ru.nyakshoot.messenger.domain.chats.Chat
import ru.nyakshoot.messenger.presentation.chats.viewmodel.ChatsState


class ChatScreenModel @AssistedInject constructor(
    @Assisted val chat: Chat,
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository
) : ScreenModel {

    @AssistedFactory
    interface Factory : ScreenModelFactory {
        fun create(chat: Chat): ChatScreenModel
    }

    private val _messagesFlow = MutableStateFlow<List<Message>?>(null)
    val messagesFlow: StateFlow<List<Message>?> = _messagesFlow

    private val _chatState = MutableLiveData<ChatState>()
    val chatState: LiveData<ChatState> get() = _chatState

    val currentAuthUser = authRepository.currentAuthUser

    private val _selectedMessages = MutableStateFlow<List<Message>>(listOf())
    val selectedMessages: StateFlow<List<Message>> = _selectedMessages


    init {
        observeMessages()
        readMessages()
    }

    private fun observeMessages() {
        screenModelScope.launch {
            _chatState.value = ChatState.MessagesLoading
            delay(1000)
            chatRepository.observeMessages(chat.id)
                .catch { e ->
                    e.printStackTrace()
                    _messagesFlow.value = emptyList()
                    _chatState.value = ChatState.MessagesLoaded
                    delay(1000)
                    observeMessages()
                }
                .collect { messages ->
                    _messagesFlow.value = messages
                    _chatState.value = ChatState.MessagesLoaded
                }
        }
    }

    fun sendNewMessage(text: String){
        screenModelScope.launch {
            chatRepository.createNewMessage(text, chat.id)
            if (_messagesFlow.value.isNullOrEmpty())
                observeMessages()
        }
    }

    fun deleteChats() = screenModelScope.launch {
        chatRepository.deleteMessage(chat.id, _selectedMessages.value)
        _selectedMessages.value = emptyList()
        _chatState.value = ChatState.MessagesLoaded
    }

    fun isSelected(message: Message): Boolean {
        return _selectedMessages.value.contains(message)
    }

    fun selectMessage(message: Message) {
        val currentSelected = _selectedMessages.value.toMutableList()
        if (currentSelected.contains(message)) {
            currentSelected.remove(message)
        } else {
            currentSelected.add(message)
        }
        _selectedMessages.value = currentSelected

        _chatState.value = if (currentSelected.isEmpty()) {
            ChatState.MessagesSelected
        } else {
            ChatState.MessagesLoaded
        }
    }

    fun resetSelection() {
        _selectedMessages.value = emptyList()
        _chatState.value = ChatState.MessagesLoaded
    }


    fun readMessages() = screenModelScope.launch {
        chatRepository.readMessages(chat.id, chat.companion.id)
    }
}