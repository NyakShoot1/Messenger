package ru.nyakshoot.messenger.core.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getScreenModel
import cafe.adriel.voyager.hilt.getViewModel
import ru.nyakshoot.messenger.presentation.chat.view.ChatScreen
import ru.nyakshoot.messenger.presentation.chat.view.composables.ChatTopAppBar
import ru.nyakshoot.messenger.presentation.chat.viewmodel.ChatScreenModel
import ru.nyakshoot.messenger.presentation.chats.view.ChatsScreen
import ru.nyakshoot.messenger.presentation.chats.view.composables.ChatsTopAppBar
import ru.nyakshoot.messenger.presentation.chats.viewmodel.ChatsViewModel

@Composable
fun TopAppBarForCurrentScreen(currentScreen: Screen) {
    when (currentScreen) {
        is ChatScreen -> {
            val screenModel = currentScreen.getScreenModel<ChatScreenModel>()
            ChatTopAppBar(screenModel)
        }
        is ChatsScreen -> {
            val viewmodel = currentScreen.getViewModel<ChatsViewModel>()
            ChatsTopAppBar(viewmodel)
        }
    }
}