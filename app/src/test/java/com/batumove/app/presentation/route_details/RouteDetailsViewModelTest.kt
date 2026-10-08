package com.batumove.app.presentation.route_details

import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.BusStop
import com.batumove.app.domain.model.BusesUpdate
import com.batumove.app.domain.model.Direction
import com.batumove.app.domain.model.GeoPoint
import com.batumove.app.domain.model.RouteGeometry
import com.batumove.app.domain.model.RouteStop
import com.batumove.app.domain.usecase.GetRouteDetailsUseCase
import com.batumove.app.usecase.FakeTransportRepository
import com.batumove.app.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


@OptIn(ExperimentalCoroutinesApi::class)
class RouteDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeTransportRepository

    private lateinit var getRouteDetailsUseCase:
            GetRouteDetailsUseCase

    private lateinit var observeBuses:
            FakeObserveRouteBuses

    private lateinit var viewModel:
            RouteDetailsViewModel

    @Before
    fun setUp() {
        repository = FakeTransportRepository()

        getRouteDetailsUseCase =
            GetRouteDetailsUseCase(repository)

        observeBuses =
            FakeObserveRouteBuses()

        viewModel =
            RouteDetailsViewModel(
                getRouteDetailsUseCase = getRouteDetailsUseCase,
                observeRouteBuses = observeBuses,
            )
    }

    @Test
    fun `route details are loaded successfully`() = runTest {
        // Arrange
        repository.routeStops = listOf(
            createStop(
                id = "stop_1",
                order = 1,
            ),
            createStop(
                id = "stop_2",
                order = 2,
            ),
        )

        repository.routeGeometry =
            RouteGeometry(
                routeId = "route_2",
                points = listOf(
                    GeoPoint(
                        latitude = 41.64,
                        longitude = 41.63,
                    ),
                    GeoPoint(
                        latitude = 41.65,
                        longitude = 41.64,
                    ),
                ),
            )

        // Act
        viewModel.loadRoute(
            routeId = "route_2",
            direction = Direction.OUTBOUND,
        )

        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)

        assertNull(state.error)

        assertEquals(
            Direction.OUTBOUND,
            state.direction,
        )

        assertEquals(
            2,
            state.stops.size,
        )

        assertEquals(
            "route_2",
            state.geometry?.routeId,
        )
    }

    @Test
    fun `bus update changes buses in ui state`() = runTest {
        // Arrange
        prepareRoute()

        viewModel.loadRoute(
            routeId = "route_2",
            direction = Direction.OUTBOUND,
        )

        advanceUntilIdle()

        val bus = createBus(
            latitude = 41.6431,
            longitude = 41.6554,
        )

        // Act
        observeBuses.emit(
            BusesUpdate.Success(
                buses = listOf(bus),
            )
        )

        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertEquals(
            1,
            state.buses.size,
        )

        assertEquals(
            "CN 406 NC",
            state.buses.first().name,
        )

        assertEquals(
            41.6431,
            state.buses.first().position.latitude,
            0.0,
        )

        assertNull(
            state.busesError,
        )
    }

    @Test
    fun `temporary buses error keeps previous buses`() = runTest {
        // Arrange
        prepareRoute()

        viewModel.loadRoute(
            routeId = "route_2",
            direction = Direction.OUTBOUND,
        )

        advanceUntilIdle()

        val bus = createBus(
            latitude = 41.6431,
            longitude = 41.6554,
        )

        observeBuses.emit(
            BusesUpdate.Success(
                buses = listOf(bus),
            )
        )

        advanceUntilIdle()

        // Act
        observeBuses.emit(
            BusesUpdate.Error(
                cause = IOException(
                    "No internet"
                ),
            )
        )

        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertEquals(
            listOf(bus),
            state.buses,
        )

        assertEquals(
            "No internet",
            state.busesError,
        )
    }

    @Test
    fun `successful bus update clears previous buses error`() = runTest {
        // Arrange
        prepareRoute()

        viewModel.loadRoute(
            routeId = "route_2",
            direction = Direction.OUTBOUND,
        )

        advanceUntilIdle()

        observeBuses.emit(
            BusesUpdate.Error(
                cause = IOException(
                    "No internet"
                ),
            )
        )

        advanceUntilIdle()

        assertEquals(
            "No internet",
            viewModel.uiState.value.busesError,
        )

        // Act
        observeBuses.emit(
            BusesUpdate.Success(
                buses = listOf(
                    createBus(
                        latitude = 41.6440,
                        longitude = 41.6570,
                    )
                ),
            )
        )

        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value

        assertNull(
            state.busesError,
        )

        assertEquals(
            1,
            state.buses.size,
        )

        assertEquals(
            41.6440,
            state.buses.first().position.latitude,
            0.0,
        )
    }

    private fun prepareRoute() {
        repository.routeStops =
            listOf(
                createStop(
                    id = "stop_1",
                    order = 1,
                ),
                createStop(
                    id = "stop_2",
                    order = 2,
                ),
            )

        repository.routeGeometry =
            RouteGeometry(
                routeId = "route_2",
                points = listOf(
                    GeoPoint(
                        latitude = 41.64,
                        longitude = 41.63,
                    ),
                    GeoPoint(
                        latitude = 41.65,
                        longitude = 41.64,
                    ),
                ),
            )
    }

    private fun createStop(
        id: String,
        order: Int,
    ): RouteStop =
        RouteStop(
            stop = BusStop(
                id = id,
                number = null,
                name = "Stop $order",
                position = GeoPoint(
                    latitude = 41.64,
                    longitude = 41.63,
                ),
            ),
            direction = Direction.OUTBOUND,
            order = order,
        )

    private fun createBus(
        latitude: Double,
        longitude: Double,
    ): Bus =
        Bus(
            name = "CN 406 NC",
            routeId = "route_2",
            position = GeoPoint(
                latitude = latitude,
                longitude = longitude,
            ),
            direction = Direction.OUTBOUND,
        )
}