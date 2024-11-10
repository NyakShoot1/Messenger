package ru.nyakshoot.messenger.presentation.splash.viewmodel

sealed class SplashState {

    data object NonAuthorized : SplashState()

    data object Authorized : SplashState()


}