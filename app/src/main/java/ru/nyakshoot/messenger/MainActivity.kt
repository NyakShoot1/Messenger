package ru.nyakshoot.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.AndroidEntryPoint
import ru.nyakshoot.messenger.core.navigation.TopAppBarForCurrentScreen
import ru.nyakshoot.messenger.presentation.chat.view.ChatScreen
import ru.nyakshoot.messenger.presentation.chats.view.ChatsScreen
import ru.nyakshoot.messenger.presentation.splash.view.SplashScreen
import ru.nyakshoot.messenger.ui.theme.MessengerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFirestore()
        enableEdgeToEdge()
        setContent {
            MessengerTheme {
                Navigator(
                    SplashScreen(),
                ) { navigation ->
                    val currentScreen = navigation.lastItem

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = { TopAppBarForCurrentScreen(currentScreen) }
                    ) { innerPadding ->
                        SlideTransition(
                            navigator = navigation,
                            modifier = Modifier.padding(innerPadding),
                            animationSpec = tween(500)
                        ) { screen ->
                            screen.Content()
                        }
                    }
                }
            }
        }
    }

    fun initFirestore() {
        val db = FirebaseFirestore.getInstance()
        val currentSettings = db.firestoreSettings

        if (!currentSettings.isPersistenceEnabled) {
            db.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
        }

    }
}