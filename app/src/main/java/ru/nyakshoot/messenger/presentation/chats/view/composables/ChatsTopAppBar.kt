package ru.nyakshoot.messenger.presentation.chats.view.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.hilt.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.nyakshoot.messenger.R
import ru.nyakshoot.messenger.domain.chats.User
import ru.nyakshoot.messenger.presentation.auth.view.AuthScreen
import ru.nyakshoot.messenger.presentation.chat.view.ChatScreen
import ru.nyakshoot.messenger.presentation.chat.viewmodel.ChatScreenModel
import ru.nyakshoot.messenger.presentation.chats.viewmodel.ChatsScreenModel
import ru.nyakshoot.messenger.presentation.chats.viewmodel.ChatsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsTopAppBar() {

    val navigator = LocalNavigator.currentOrThrow

    val screenModel = navigator.getNavigatorScreenModel<ChatsScreenModel>()

    var showDialog by remember { mutableStateOf(false) }
    val users by screenModel.users.collectAsState()
    val chatsState by screenModel.chatsState.observeAsState()
    val selectedChats by screenModel.selectedChats.collectAsState()

    Log.d("current_selected_chats", selectedChats.toString())

    Column {
        TopAppBar(
            title = {
                when(chatsState){
                    ChatsState.ChatsSelected -> { Text("Выбрано: ${selectedChats.size}") }
                    else -> { Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.baseline_message_24),
                            contentDescription = "chats",
                            modifier = Modifier.size(30.dp)
                        )
                        Text("Чаты") }
                    }
                }
            },
            navigationIcon = {
                when (chatsState) {
                    ChatsState.ChatsSelected -> {
                        IconButton(
                            onClick = {
                                screenModel.resetSelection()
                            }
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "close selection"
                            )
                        }
                    }
                    else -> {}
                }
            },
            actions = {
                when (chatsState) {
                    ChatsState.ChatsSelected -> {
                        IconButton(
                            onClick = {
                                screenModel.deleteChats()
                            }
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "delete chat"
                            )
                        }
                    }

                    else -> {
                        IconButton(
                            onClick = {
                                showDialog = true
                            }
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add chat"
                            )
                        }
                        IconButton(
                            onClick = {
                                navigator.replaceAll(AuthScreen())
                                screenModel.logOut()
                            }
                        ) {
                            Icon(
                                painterResource(R.drawable.baseline_logout_24),
                                contentDescription = "Exit app"
                            )
                        }
                    }
                }
            },
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
    }

    LaunchedEffect(showDialog) {
        when (showDialog) {
            true -> {
                screenModel.getUsers()
            }

            false -> Unit
        }
    }

    if (showDialog) {
        CreateNewChatDialog(
            users = users ?: emptyList(),
            onSelectedUser = { user ->
                screenModel.createNewChat(user)
                showDialog = false
            },
            onDismissRequest = { showDialog = false })
    }
}

@Composable
private fun CreateNewChatDialog(
    users: List<User>,
    onSelectedUser: (User) -> Unit,
    onDismissRequest: () -> Unit
) {

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier.fillMaxSize(0.96f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                item {
                    Text("Пользователи: ")
                }
                items(users) { user ->
                    UserCard(user.username) { onSelectedUser(user) }
                }
            }
        }
    }

}

@Composable
private fun UserCard(
    username: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(15.dp)
            .height(45.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = username,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun UserCardPreview() {
    UserCard("NyakShoot") {}
}