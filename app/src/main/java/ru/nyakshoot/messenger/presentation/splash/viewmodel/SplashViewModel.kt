package ru.nyakshoot.messenger.presentation.splash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.nyakshoot.messenger.domain.auth.AuthManager
import ru.nyakshoot.messenger.domain.auth.isAuthorized
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

    private val _splashState = MutableLiveData(
        if (authManager.isAuthorized()) {
            SplashState.Authorized
        } else {
            SplashState.NonAuthorized
        }
    )

    val splashState: LiveData<SplashState> get() = _splashState
}