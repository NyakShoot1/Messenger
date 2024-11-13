package ru.nyakshoot.messenger.presentation.chats.view

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.hilt.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.nyakshoot.messenger.domain.chat.Message
import ru.nyakshoot.messenger.presentation.chat.view.ChatScreen
import ru.nyakshoot.messenger.presentation.chat.viewmodel.ChatScreenModel
import ru.nyakshoot.messenger.presentation.chats.view.composables.ChatCard
import ru.nyakshoot.messenger.presentation.chats.viewmodel.ChatsState
import ru.nyakshoot.messenger.presentation.chats.viewmodel.ChatsScreenModel

class ChatsScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val screenModel =  getScreenModel<ChatsScreenModel, ChatsScreenModel.Factory>{ factory ->
            factory.create()
        }

        navigator.rememberNavigatorScreenModel { screenModel }

        val chats by screenModel.chatsFlow.collectAsState()
        val selectedChats by screenModel.selectedChats.collectAsState()
        val chatsState by screenModel.chatsState.observeAsState()

        Log.d("chat", screenModel.toString())

        BackHandler(enabled = chatsState == ChatsState.ChatsSelected) {
            screenModel.resetSelection()
        }

        LaunchedEffect(selectedChats){}

        when (chatsState) {
            ChatsState.ChatsLoaded -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (chats.isNullOrEmpty()) {
                        true -> {
                            item {
                                Text("No chats")
                            }
                        }

                        false -> {
                            items(chats!!) { chat ->
                                ChatCard(
                                    chat.companion,
                                    chat.lastMessage ?: Message(text = "No messages"),
                                    screenModel.currentUser,
                                    false,
                                    onClick = {
                                        navigator.push(ChatScreen(chat))
                                    },
                                    onLongClick = {
                                        screenModel.selectChat(chat)
                                    }
                                )
                                HorizontalDivider(modifier = Modifier.fillMaxWidth(0.95f))
                            }
                        }
                    }
                }
            }

            ChatsState.ChatsLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            ChatsState.ChatsSelected -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(chats!!) { chat ->
                        val isSelected = screenModel.isSelected(chat)
                        ChatCard(
                            chat.companion,
                            chat.lastMessage ?: Message(text = "No messages"),
                            screenModel.currentUser,
                            isSelected,
                            onClick = {
                                screenModel.selectChat(chat)
                            },
                            onLongClick = {
                                screenModel.selectChat(chat)
                            }
                        )
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.95f))
                    }
                }
            }

            else -> Unit
        }
    }
}