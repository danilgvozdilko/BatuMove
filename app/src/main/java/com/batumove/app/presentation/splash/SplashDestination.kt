package com.batumove.app.presentation.splash

sealed interface SplashDestination {

    data object Language : SplashDestination

    data object Main : SplashDestination
}