package ru.nyakshoot.messenger.presentation.auth.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ru.nyakshoot.messenger.presentation.auth.viewmodel.AuthState
import ru.nyakshoot.messenger.presentation.auth.viewmodel.AuthViewModel
import ru.nyakshoot.messenger.presentation.chats.view.ChatsScreen

class AuthScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: AuthViewModel = getViewModel()
        val authState = viewModel.authState.observeAsState()

        val context = LocalContext.current
        var isLoading by rememberSaveable { mutableStateOf(false) }
        var isRegistration by rememberSaveable { mutableStateOf(false) }
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var username by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isRegistration) "Registration" else "Log in"
            )

            if (isRegistration) {
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        viewModel.resetAuthState()
                    },
                    label = { Text("Username") }, // todo res
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    supportingText = {
                        if (authState.value == AuthState.InputError.Username) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Username error",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    trailingIcon = {
                        if (authState.value == AuthState.InputError.Email)
                            Icon(
                                Icons.Outlined.Warning,
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                    }
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.resetAuthState()
                },
                label = { Text("Email") }, // todo res
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                trailingIcon = {
                    if (authState.value == AuthState.InputError.Email)
                        Icon(
                            Icons.Outlined.Warning,
                            "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                }
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.resetAuthState()
                },
                label = { Text("Password") }, // todo res
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation(),
                supportingText = {
                    if (authState.value == AuthState.InputError.Password) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Password error",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                trailingIcon = {
                    if (authState.value == AuthState.InputError.Password)
                        Icon(
                            Icons.Outlined.Warning,
                            "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                }
            )
            Button(
                onClick = {
                    if (isRegistration)
                        viewModel.register(email, username, password)
                    else
                        viewModel.logIn(email, password)
                },
                modifier = Modifier.width(250.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.Yellow
                    )
                } else if (!isRegistration) {
                    Text("Log in") // todo res
                } else {
                    Text("Register")
                }
            }
            TextButton(
                onClick = { isRegistration = !isRegistration }
            ) {
                Text(
                    text = if (isRegistration) "ALREADY HAVE ACCOUNT" else "CREATE NEW ACCOUNT",
                    color = Color.Blue
                )
            }
        }

        LaunchedEffect(authState.value) {
            when (authState.value) {
                is AuthState.AuthError -> {
                    viewModel.resetAuthState()
                    isLoading = false
                    Toast.makeText(
                        context,
                        (authState.value as AuthState.AuthError).logInError,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is AuthState.Authorized -> {
                    isLoading = false
                    navigator.replaceAll(ChatsScreen())
                }

                is AuthState.Authorizing -> {
                    isLoading = true
                }

                else -> Unit
            }
        }
    }

}