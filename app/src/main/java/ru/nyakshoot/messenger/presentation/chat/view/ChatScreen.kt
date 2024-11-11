package ru.nyakshoot.messenger.presentation.chat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.hilt.getScreenModel
import ru.nyakshoot.messenger.domain.chats.Chat
import ru.nyakshoot.messenger.presentation.chat.view.composables.DateDivider
import ru.nyakshoot.messenger.presentation.chat.view.composables.MessageCard
import ru.nyakshoot.messenger.presentation.chat.viewmodel.ChatState
import ru.nyakshoot.messenger.presentation.chat.viewmodel.ChatScreenModel
import ru.nyakshoot.messenger.utils.formatMessageDate

class ChatScreen(
    val chat: Chat,
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {

        val screenModel = getScreenModel<ChatScreenModel, ChatScreenModel.Factory> { factory ->
            factory.create(chat)
        }

        val messages by screenModel.messagesFlow.collectAsState()
        val chatState by screenModel.chatState.observeAsState()
        val currentUser = screenModel.currentAuthUser

        var text by rememberSaveable { mutableStateOf("") }

        val listState = rememberLazyListState()

        LaunchedEffect(messages) {
            if (!messages.isNullOrEmpty()) {
                listState.animateScrollToItem(messages!!.size - 1)
            }
            screenModel.readMessages()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (chatState) {
                ChatState.MessagesLoaded -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = listState
                    ) {
                        when (messages.isNullOrEmpty()) {
                            true -> {
                                item {
                                    Text("No messages")
                                }
                            }

                            false -> {
                                val groupedMessages = messages!!.groupBy { message ->
                                    formatMessageDate(message.ts!!)
                                }

                                groupedMessages.forEach { (date, messagesForDate) ->
                                    item {
                                        DateDivider(date = date)
                                    }
                                    items(messagesForDate) { message ->
                                        MessageCard(message, currentUser!!.id)
                                    }
                                }
                            }
                        }
                    }
                }

                ChatState.MessagesLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                null -> Unit
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Gray.copy(alpha = 0.1f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    maxLines = 2,
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
                )

                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            screenModel.sendNewMessage(text)
                            text = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Message"
                    )
                }
            }
        }
    }
}