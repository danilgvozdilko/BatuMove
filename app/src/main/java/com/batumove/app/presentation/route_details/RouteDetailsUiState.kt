package com.batumove.app.presentation.route_details

import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.Direction
import com.batumove.app.domain.model.GeoPoint
import com.batumove.app.domain.model.NextBusArrival
import com.batumove.app.domain.model.Route
import com.batumove.app.domain.model.RouteGeometry
import com.batumove.app.domain.model.RouteStop


data class RouteDetailsUiState(

    val isLoading: Boolean = true,

    val routeId: String = "",

    val route: Route? = null,

    val direction: Direction =
        Direction.OUTBOUND,

    val stops: List<RouteStop> =
        emptyList(),

    val geometry: RouteGeometry? =
        null,

    val buses: List<Bus> =
        emptyList(),

    /*
     * Остановка, которую пользователь
     * сейчас нажал непосредственно
     * на RouteDetails.
     */
    val selectedStopId: String? =
        null,

    /*
     * Стартовая остановка,
     * пришедшая из Trip Planner.
     *
     * Она остаётся специально выделенной.
     */
    val highlightedStopId: String? =
        null,

    /*
     * Если RouteDetails открыт
     * через конкретный автобус.
     */
    val selectedBusName: String? =
        null,

    /*
     * Location.
     */
    val userLocation: GeoPoint? =
        null,

    val locationPermissionGranted: Boolean =
        false,

    val nextBusArrival: NextBusArrival? =
        null,

    val busesError: Boolean =
        false,

    val hasError: Boolean =
        false,
) {

    val directionBuses: List<Bus>
        get() =
            buses.filter { bus ->
                bus.direction == direction
            }

    val selectedStop: RouteStop?
        get() =
            stops.firstOrNull { routeStop ->
                routeStop.stop.id ==
                        selectedStopId
            }

    val highlightedStop: RouteStop?
        get() =
            stops.firstOrNull { routeStop ->
                routeStop.stop.id ==
                        highlightedStopId
            }
}