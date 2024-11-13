package ru.nyakshoot.messenger.presentation.chat.view.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.hilt.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.nyakshoot.messenger.presentation.chat.viewmodel.ChatScreenModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar() {

    val navigator = LocalNavigator.currentOrThrow
    val screenModel = navigator.getNavigatorScreenModel<ChatScreenModel>()

    TopAppBar(
        title = { Text(
            screenModel.chat.companion.username,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        ) },
        navigationIcon = {
            IconButton(
                onClick = {
                    navigator.pop()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}