package com.batumove.app.presentation.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batumove.app.data.location.LocationDataSource
import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.BusStop
import com.batumove.app.domain.model.GeoPoint
import com.batumove.app.domain.model.TripOption
import com.batumove.app.domain.repository.TransportRepository
import com.batumove.app.domain.usecase.GetNearbyStopsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: TransportRepository,
    private val locationDataSource: LocationDataSource,
    private val getNearbyStops: GetNearbyStopsUseCase,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(MapUiState())

    val uiState: StateFlow<MapUiState> =
        _uiState.asStateFlow()

    private var busesJob: Job? = null

    init {
        loadMap()
    }

    private fun loadMap() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    hasError = false,
                )
            }

            try {
                val routes =
                    repository.getRoutes()

                val stops =
                    repository.getStops()

                val currentLocation =
                    _uiState.value.userLocation

                val nearbyStops =
                    if (currentLocation != null) {
                        getNearbyStops(
                            userLocation = currentLocation,
                            stops = stops,
                        )
                    } else {
                        emptyList()
                    }

                _uiState.update {
                    it.copy(
                        routes = routes,
                        stops = stops,
                        nearbyStops = nearbyStops,
                        isLoading = false,
                    )
                }

                observeBuses()

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hasError = true,
                    )
                }
            }
        }
    }

    private fun filterNearbyBuses(
        buses: List<Bus>,
        userLocation: GeoPoint?,
        radiusMeters: Int = 2_000,
    ): List<Bus> {
        if (userLocation == null) {
            return emptyList()
        }

        return buses.filter { bus ->
            val result = FloatArray(1)

            Location.distanceBetween(
                userLocation.latitude,
                userLocation.longitude,
                bus.position.latitude,
                bus.position.longitude,
                result,
            )

            result[0] <= radiusMeters
        }
    }

    fun onLocationPermissionChanged(
        granted: Boolean,
    ) {
        _uiState.update {
            it.copy(
                locationPermissionGranted = granted,
            )
        }

        if (granted) {
            loadCurrentLocation()
        }
    }

    fun refreshLocation() {
        if (!_uiState.value.locationPermissionGranted) {
            return
        }

        loadCurrentLocation()
    }

    private fun loadCurrentLocation() {
        viewModelScope.launch {

            val location =
                locationDataSource
                    .getCurrentLocation()
                    ?: return@launch

            val state =
                _uiState.value

            val nearbyStops =
                getNearbyStops(
                    userLocation = location,
                    stops = state.stops,
                )

            val nearbyBuses =
                filterNearbyBuses(
                    buses = state.buses,
                    userLocation = location,
                )

            _uiState.update {
                it.copy(
                    userLocation = location,
                    nearbyStops = nearbyStops,
                    nearbyBuses = nearbyBuses,
                )
            }
        }
    }

    private fun observeBuses() {
        busesJob?.cancel()

        busesJob = viewModelScope.launch {
            while (isActive) {
                try {
                    val buses =
                        repository.getBuses()

                    val nearbyBuses =
                        filterNearbyBuses(
                            buses = buses,
                            userLocation = _uiState.value.userLocation,
                        )

                    _uiState.update {
                        it.copy(
                            buses = buses,
                            nearbyBuses = nearbyBuses,
                        )
                    }


                } catch (exception: CancellationException) {
                    throw exception

                } catch (_: Exception) {
                    // Не очищаем старые позиции при
                    // временной ошибке realtime API.
                }

                delay(10.seconds)
            }
        }
    }

    fun selectStop(
        stopId: String?,
    ) {
        _uiState.update {
            it.copy(
                selectedStopId = stopId,
                selectedBusKey = null,
            )
        }
    }

    fun selectBus(
        busKey: String?,
    ) {
        _uiState.update {
            it.copy(
                selectedBusKey = busKey,
                selectedStopId = null,
            )
        }
    }

    fun retry() {
        busesJob?.cancel()
        loadMap()
    }

    fun onDestinationQueryChanged(
        query: String,
    ) {
        val normalized =
            query.trim()

        val suggestions =
            if (normalized.length < 2) {
                emptyList()
            } else {
                _uiState.value.stops
                    .asSequence()
                    .filter { stop ->
                        stop.name.contains(
                            normalized,
                            ignoreCase = true,
                        )
                    }
                    .take(6)
                    .toList()
            }

        _uiState.update {
            it.copy(
                destinationQuery = query,
                destinationSuggestions = suggestions,
            )
        }
    }

    fun selectDestination(
        stop: BusStop,
    ) {
        _uiState.update {
            it.copy(
                destinationStop = stop,
                destinationQuery = stop.name,
                destinationSuggestions = emptyList(),
            )
        }

        findTripOptions(stop)
    }

    private fun findTripOptions(
        destinationStop: BusStop,
    ) {
        viewModelScope.launch {

            val state =
                _uiState.value

            val nearbyStops =
                state.nearbyStops

            if (nearbyStops.isEmpty()) {
                return@launch
            }

            _uiState.update {
                it.copy(
                    isSearchingTrip = true,
                    tripOptions = emptyList(),
                )
            }

            try {
                val options =
                    nearbyStops
                        .take(5)
                        .flatMap { nearbyStart ->

                            repository
                                .getDirectRoutes(
                                    startStopId =
                                        nearbyStart.stop.id,

                                    destinationStopId =
                                        destinationStop.id,
                                )
                                .map { route ->
                                    TripOption(
                                        route = route,
                                        startStop =
                                            nearbyStart.stop,
                                        destinationStop =
                                            destinationStop,
                                        distanceToStartMeters =
                                            nearbyStart.distanceMeters,
                                    )
                                }
                        }
                        .distinctBy { option ->
                            option.route.id
                        }
                        .sortedBy { option ->
                            option.distanceToStartMeters
                        }

                _uiState.update {
                    it.copy(
                        tripOptions = options,
                        isSearchingTrip = false,
                    )
                }

            } catch (_: Exception) {

                _uiState.update {
                    it.copy(
                        tripOptions = emptyList(),
                        isSearchingTrip = false,
                    )
                }
            }
        }
    }
}