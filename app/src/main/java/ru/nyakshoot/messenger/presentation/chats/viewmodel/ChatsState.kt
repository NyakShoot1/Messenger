package ru.nyakshoot.messenger.presentation.chats.viewmodel

sealed class ChatsState {

    data object ChatsLoading: ChatsState()

    data object ChatsLoaded: ChatsState()

}