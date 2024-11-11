package ru.nyakshoot.messenger.presentation.chats.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.nyakshoot.messenger.R
import ru.nyakshoot.messenger.domain.chats.User
import ru.nyakshoot.messenger.presentation.auth.view.AuthScreen
import ru.nyakshoot.messenger.presentation.chats.viewmodel.ChatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsTopAppBar(viewmodel: ChatsViewModel) {
    val navigator = LocalNavigator.currentOrThrow
    var showDialog by remember { mutableStateOf(false) }
    val users by viewmodel.users.collectAsState()

    Column {
        TopAppBar(
            title = {
                Text("Чаты")
            },
            actions = {
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
                        viewmodel.logOut()
                    }
                ) {
                    Icon(
                        painterResource(R.drawable.baseline_logout_24),
                        contentDescription = "Exit app"
                    )
                }
            },
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
    }

    LaunchedEffect(showDialog) {
        when (showDialog) {
            true -> {
                viewmodel.getUsers()
            }

            false -> Unit
        }
    }

    if (showDialog) {
        CreateNewChatDialog(
            users = users ?: emptyList(),
            onSelectedUser = { user ->
                viewmodel.createNewChat(user)
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
                modifier = Modifier.fillMaxSize().padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
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