package com.batumove.app.presentation.route_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batumove.app.data.location.LocationDataSource
import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.BusPositionSample
import com.batumove.app.domain.model.BusesUpdate
import com.batumove.app.domain.model.Direction
import com.batumove.app.domain.usecase.CalculateBusEtaUseCase
import com.batumove.app.domain.usecase.CalculateStopArrivalsUseCase
import com.batumove.app.domain.usecase.GetRouteDetailsUseCase
import com.batumove.app.domain.usecase.ObserveRouteBusesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class RouteDetailsViewModel @Inject constructor(

    private val getRouteDetailsUseCase:
    GetRouteDetailsUseCase,

    private val observeRouteBusesUseCase:
    ObserveRouteBusesUseCase,

    private val calculateNextBusArrivalUseCase:
    CalculateStopArrivalsUseCase,

    private val calculateBusEtaUseCase:
    CalculateBusEtaUseCase,

    private val locationDataSource:
    LocationDataSource,

    ) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            RouteDetailsUiState()
        )

    val uiState:
            StateFlow<RouteDetailsUiState> =
        _uiState.asStateFlow()

    private var busesJob: Job? =
        null

    private var routeDetailsJob: Job? =
        null

    private val busPositionHistory =
        mutableMapOf<
                String,
                MutableList<BusPositionSample>
                >()

    fun loadRoute(
        routeId: String,
        selectedBusName: String? = null,
        selectedStopId: String? = null,
        direction: Direction =
            Direction.OUTBOUND,
    ) {
        val currentState =
            _uiState.value

        _uiState.update { state ->
            state.copy(
                selectedBusName =
                    selectedBusName,

                highlightedStopId =
                    selectedStopId,

                selectedStopId =
                    selectedStopId,

                direction =
                    direction,
            )
        }

        if (
            currentState.routeId == routeId &&
            currentState.route != null
        ) {
            return
        }

        loadRouteDetails(
            routeId = routeId,
            direction = direction,
            initialSelectedStopId =
                selectedStopId,
        )

        observeBuses(
            routeId = routeId,
        )
    }

    fun changeDirection(
        direction: Direction,
    ) {
        val currentState =
            _uiState.value

        if (
            currentState.routeId.isBlank() ||
            currentState.direction ==
            direction
        ) {
            return
        }

        /*
         * Пользователь вручную сменил
         * направление.
         *
         * Контекст Trip Planner
         * больше нельзя считать валидным.
         */
        _uiState.update { state ->
            state.copy(
                direction = direction,
                selectedStopId = null,
                highlightedStopId = null,
                selectedBusName = null,
                nextBusArrival = null,
            )
        }

        loadRouteDetails(
            routeId =
                currentState.routeId,

            direction =
                direction,

            initialSelectedStopId =
                null,
        )
    }

    fun selectStop(
        stopId: String?,
    ) {
        _uiState.update { state ->
            state.copy(
                selectedStopId = stopId,
                nextBusArrival = null,
            )
        }

        updateArrival()
    }

    fun onLocationPermissionChanged(
        granted: Boolean,
    ) {
        _uiState.update {
            it.copy(
                locationPermissionGranted =
                    granted,
            )
        }

        if (granted) {
            refreshLocation()
        }
    }

    fun refreshLocation() {
        if (
            !_uiState.value
                .locationPermissionGranted
        ) {
            return
        }

        viewModelScope.launch {

            val location =
                locationDataSource
                    .getCurrentLocation()
                    ?: return@launch

            _uiState.update {
                it.copy(
                    userLocation = location,
                )
            }
        }
    }

    fun retry() {
        val currentState =
            _uiState.value

        if (
            currentState.routeId.isBlank()
        ) {
            return
        }

        loadRouteDetails(
            routeId =
                currentState.routeId,

            direction =
                currentState.direction,

            initialSelectedStopId =
                currentState
                    .highlightedStopId,
        )

        observeBuses(
            routeId =
                currentState.routeId,
        )
    }

    private fun loadRouteDetails(
        routeId: String,
        direction: Direction,
        initialSelectedStopId: String?,
    ) {
        routeDetailsJob?.cancel()

        routeDetailsJob =
            viewModelScope.launch {

                val currentRouteId =
                    _uiState.value.routeId

                val isNewRoute =
                    currentRouteId != routeId

                _uiState.update { state ->
                    state.copy(
                        isLoading = true,
                        routeId = routeId,
                        direction = direction,

                        selectedStopId =
                            initialSelectedStopId,

                        nextBusArrival = null,

                        buses =
                            if (isNewRoute) {
                                emptyList()
                            } else {
                                state.buses
                            },

                        busesError =
                            if (isNewRoute) {
                                false
                            } else {
                                state.busesError
                            },

                        hasError = false,
                    )
                }

                if (isNewRoute) {
                    busPositionHistory.clear()
                }

                try {

                    val details =
                        getRouteDetailsUseCase(
                            routeId = routeId,
                            direction = direction,
                        )

                    val validHighlightedStopId =
                        initialSelectedStopId
                            ?.takeIf { stopId ->

                                details.stops.any {
                                    it.stop.id ==
                                            stopId
                                }
                            }

                    _uiState.update { state ->

                        state.copy(
                            isLoading = false,

                            route =
                                details.route,

                            stops =
                                details.stops,

                            geometry =
                                details.geometry,

                            selectedStopId =
                                validHighlightedStopId,

                            highlightedStopId =
                                state
                                    .highlightedStopId
                                    ?.takeIf { highlightId ->

                                        details.stops.any { routeStop ->

                                            routeStop
                                                .stop
                                                .id ==
                                                    highlightId
                                        }
                                    },

                            hasError = false,
                        )
                    }

                    updateArrival()

                } catch (
                    exception:
                    CancellationException
                ) {
                    throw exception

                } catch (
                    exception: Exception
                ) {

                    Log.e(
                        TAG,
                        "Failed to load route details",
                        exception,
                    )

                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            hasError = true,
                        )
                    }
                }
            }
    }

    private fun observeBuses(
        routeId: String,
    ) {
        busesJob?.cancel()

        busesJob =
            viewModelScope.launch {

                observeRouteBusesUseCase(
                    routeId = routeId,
                ).collect { update ->

                    when (update) {

                        is BusesUpdate.Success -> {

                            Log.d(
                                TAG,
                                "Buses received: " +
                                        "${update.buses.size}, " +
                                        "routeId=$routeId",
                            )

                            updateBusPositionHistory(
                                buses =
                                    update.buses,
                            )

                            _uiState.update { state ->
                                state.copy(
                                    buses =
                                        update.buses,

                                    busesError =
                                        false,
                                )
                            }

                            updateArrival()
                        }

                        is BusesUpdate.Error -> {

                            Log.e(
                                TAG,
                                "Failed to update buses " +
                                        "for routeId=$routeId",
                                update.cause,
                            )

                            _uiState.update { state ->
                                state.copy(
                                    busesError = true,
                                )
                            }
                        }
                    }
                }
            }
    }

    private fun updateBusPositionHistory(
        buses: List<Bus>,
    ) {
        val now =
            System.currentTimeMillis()

        val cutoff =
            now -
                    POSITION_HISTORY_WINDOW_MS

        buses.forEach { bus ->

            val key =
                bus.stableKey()

            val history =
                busPositionHistory
                    .getOrPut(key) {
                        mutableListOf()
                    }

            history +=
                BusPositionSample(
                    position =
                        bus.position,

                    timestampMillis =
                        now,
                )

            history.removeAll { sample ->
                sample.timestampMillis <
                        cutoff
            }
        }

        val activeBusKeys =
            buses.mapTo(
                mutableSetOf()
            ) {
                it.stableKey()
            }

        busPositionHistory
            .keys
            .toList()
            .filterNot { key ->
                key in activeBusKeys
            }
            .forEach { key ->
                busPositionHistory.remove(key)
            }
    }

    private fun updateArrival() {
        val state =
            _uiState.value

        val selectedStop =
            state.selectedStop

        val geometry =
            state.geometry

        if (
            selectedStop == null ||
            geometry == null
        ) {
            _uiState.update {
                it.copy(
                    nextBusArrival = null
                )
            }

            return
        }

        val arrival =
            calculateNextBusArrivalUseCase(
                selectedStop =
                    selectedStop,

                routeStops =
                    state.stops,

                buses =
                    state.directionBuses,

                geometry =
                    geometry,
            )

        if (arrival == null) {

            _uiState.update {
                it.copy(
                    nextBusArrival = null
                )
            }

            return
        }

        val history =
            busPositionHistory[
                arrival.bus.stableKey()
            ].orEmpty()

        val etaMinutes =
            calculateBusEtaUseCase(
                distanceMeters =
                    arrival.distanceMeters,

                samples =
                    history,
            )

        Log.d(
            TAG,
            "Next bus=${arrival.bus.name}, " +
                    "distance=${arrival.distanceMeters}m, " +
                    "stops=${arrival.stopsAway}, " +
                    "samples=${history.size}, " +
                    "eta=$etaMinutes",
        )

        _uiState.update { state ->
            state.copy(
                nextBusArrival =
                    arrival.copy(
                        etaMinutes =
                            etaMinutes,
                    )
            )
        }
    }

    private fun Bus.stableKey(): String =
        "$routeId:$name"

    private companion object {

        const val TAG =
            "RouteDetails"

        const val POSITION_HISTORY_WINDOW_MS =
            60_000L
    }
}