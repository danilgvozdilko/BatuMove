package com.batumove.app.domain.model

data class RouteDetails(
    val route: Route,
    val stops: List<RouteStop>,
    val geometry: RouteGeometry,
)