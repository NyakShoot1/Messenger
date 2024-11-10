package ru.nyakshoot.messenger.presentation.chat.viewmodel

sealed class ChatState {

    data object MessagesLoading: ChatState()

    data object MessagesLoaded: ChatState()

}