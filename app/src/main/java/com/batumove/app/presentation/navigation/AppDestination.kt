package com.batumove.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AppDestination {

    @Serializable
    data object Splash : AppDestination

    @Serializable
    data object Language : AppDestination

    @Serializable
    data object Main : AppDestination

    @Serializable
    data object Settings : AppDestination

    @Serializable
    data object Map : AppDestination

    @Serializable
    data class RouteDetails(
        val routeId: String,
        val selectedBusName: String? = null,
        val selectedBusDirection: String? = null,
        val selectedStopId: String? = null,
    ) : AppDestination
}