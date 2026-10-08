package com.batumove.app.domain.model

data class RouteGeometry(
    val routeId: String,
    val points: List<GeoPoint>,
)