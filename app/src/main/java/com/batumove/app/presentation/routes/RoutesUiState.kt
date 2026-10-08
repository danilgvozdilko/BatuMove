package com.batumove.app.presentation.routes

import com.batumove.app.domain.model.Route

sealed interface RoutesUiState {

    data object Loading :
        RoutesUiState

    data class Content(
        val routes: List<Route>,
    ) : RoutesUiState

    data object Empty :
        RoutesUiState

    data object Error :
        RoutesUiState
}