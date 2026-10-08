package com.batumove.app.presentation.map

import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.BusStop
import com.batumove.app.domain.model.GeoPoint
import com.batumove.app.domain.model.NearbyStop
import com.batumove.app.domain.model.Route
import com.batumove.app.domain.model.TripOption


data class MapUiState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,

    val routes: List<Route> = emptyList(),
    val stops: List<BusStop> = emptyList(),

    val nearbyStops: List<NearbyStop> =
        emptyList(),

    val buses: List<Bus> =
        emptyList(),

    val nearbyBuses: List<Bus> =
        emptyList(),

    val userLocation: GeoPoint? = null,

    val locationPermissionGranted:
    Boolean = false,

    val selectedStopId: String? = null,
    val selectedBusKey: String? = null,

    val destinationQuery: String = "",

    val destinationSuggestions:
    List<BusStop> = emptyList(),

    val destinationStop:
    BusStop? = null,

    val tripOptions:
    List<TripOption> = emptyList(),

    val isSearchingTrip:
    Boolean = false,
)

