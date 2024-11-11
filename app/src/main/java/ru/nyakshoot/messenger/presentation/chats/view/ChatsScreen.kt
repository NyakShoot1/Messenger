package ru.nyakshoot.messenger.presentation.chats.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.nyakshoot.messenger.domain.chat.Message
import ru.nyakshoot.messenger.presentation.chat.view.ChatScreen
import ru.nyakshoot.messenger.presentation.chats.view.composables.ChatCard
import ru.nyakshoot.messenger.presentation.chats.viewmodel.ChatsState
import ru.nyakshoot.messenger.presentation.chats.viewmodel.ChatsViewModel

class ChatsScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val viewModel: ChatsViewModel = getViewModel()
        val chats by viewModel.chatsFlow.collectAsState()
        val chatsState by viewModel.chatsState.observeAsState()

        when(chatsState){
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
                                    chat.receiverUser.username,
                                    chat.lastMessage ?: Message(text = "No messages"),
                                    viewModel.currentUser!!
                                ){
                                    navigator.push(ChatScreen(chat))
                                }
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
                ){
                    CircularProgressIndicator()
                }
            }
            null -> Unit
        }
    }
}