package ru.nyakshoot.messenger.presentation.splash.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import ru.nyakshoot.messenger.presentation.auth.view.AuthScreen
import ru.nyakshoot.messenger.presentation.chats.view.ChatsScreen
import ru.nyakshoot.messenger.presentation.splash.viewmodel.SplashState
import ru.nyakshoot.messenger.presentation.splash.viewmodel.SplashViewModel

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: SplashViewModel = getViewModel()
        val authState = viewModel.splashState.observeAsState()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }

        LaunchedEffect(authState.value) {
            delay(1000)
            when (authState.value) {
                SplashState.Authorized -> {
                    navigator.replaceAll(ChatsScreen())
                }

                SplashState.NonAuthorized -> {
                    navigator.replace(AuthScreen())
                }

                null -> Unit
            }
        }
    }
}