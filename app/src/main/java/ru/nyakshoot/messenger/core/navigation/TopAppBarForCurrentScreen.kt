package ru.nyakshoot.messenger.core.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getScreenModel
import ru.nyakshoot.messenger.presentation.chat.view.ChatScreen
import ru.nyakshoot.messenger.presentation.chat.view.composables.ChatTopAppBar
import ru.nyakshoot.messenger.presentation.chat.viewmodel.ChatScreenModel
import ru.nyakshoot.messenger.presentation.chats.view.ChatsScreen
import ru.nyakshoot.messenger.presentation.chats.view.composables.ChatsTopAppBar

@Composable
fun TopAppBarForCurrentScreen(currentScreen: Screen) {
    when (currentScreen) {
        is ChatScreen -> {
            ChatTopAppBar()
        }
        is ChatsScreen -> {
            ChatsTopAppBar()
        }
    }
}