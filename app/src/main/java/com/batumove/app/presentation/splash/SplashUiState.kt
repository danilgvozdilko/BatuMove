package com.batumove.app.presentation.splash

sealed interface SplashUiState {

    data object Loading : SplashUiState

    data class Ready(
        val destination: SplashDestination,
    ) : SplashUiState

    data object Error : SplashUiState
}