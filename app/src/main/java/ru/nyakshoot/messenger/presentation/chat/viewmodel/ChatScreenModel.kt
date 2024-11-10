package ru.nyakshoot.messenger.presentation.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.hilt.ScreenModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.nyakshoot.messenger.data.auth.AuthRepository
import ru.nyakshoot.messenger.data.chat.ChatRepository
import ru.nyakshoot.messenger.domain.chat.Message
import javax.inject.Inject


class ChatScreenModel @AssistedInject constructor(
    @Assisted val chatId: String,
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository
) : ScreenModel {

    @AssistedFactory
    interface Factory : ScreenModelFactory {
        fun create(chatId: String): ChatScreenModel
    }

    private val _messagesFlow = MutableStateFlow<List<Message>?>(null)
    val messagesFlow: StateFlow<List<Message>?> = _messagesFlow

    private val _chatState = MutableLiveData<ChatState>()
    val chatState: LiveData<ChatState> get() = _chatState

    val currentAuthUser = authRepository.currentAuthUser

    init {
        observeMessages()
    }

    private fun observeMessages() {
        screenModelScope.launch {
            _chatState.value = ChatState.MessagesLoading
            delay(1500)
            chatRepository.observeMessages(chatId)
                .catch { e ->
                    e.printStackTrace()
                    _messagesFlow.value = emptyList()
                    _chatState.value = ChatState.MessagesLoaded
                }
                .collect { messages ->
                    _messagesFlow.value = messages
                    _chatState.value = ChatState.MessagesLoaded
                }
        }
    }

    fun sendNewMessage(text: String){
        screenModelScope.launch {
            chatRepository.createNewMessage(text, chatId)
        }
    }
}