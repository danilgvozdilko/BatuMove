package com.batumove.app.usecase

import app.cash.turbine.test
import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.BusesUpdate
import com.batumove.app.domain.model.Direction
import com.batumove.app.domain.model.GeoPoint
import com.batumove.app.domain.usecase.ObserveRouteBusesUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class ObserveRouteBusesUseCaseTest {

    private lateinit var repository: FakeTransportRepository
    private lateinit var useCase: ObserveRouteBusesUseCase

    @Before
    fun setUp() {
        repository = FakeTransportRepository()

        useCase = ObserveRouteBusesUseCase(
            repository = repository,
        )
    }

    @Test
    fun `buses are refreshed after interval`() = runTest {

        repository.busesResults.add(
            Result.success(
                listOf(
                    createBus(
                        latitude = 41.6431,
                        longitude = 41.6554,
                    )
                )
            )
        )

        repository.busesResults.add(
            Result.success(
                listOf(
                    createBus(
                        latitude = 41.6435,
                        longitude = 41.6561,
                    )
                )
            )
        )

        useCase(
            routeId = "route_2",
        ).test {

            val first =
                awaitItem()

            require(
                first is BusesUpdate.Success
            )

            assertEquals(
                41.6431,
                first.buses.first()
                    .position.latitude,
                0.0,
            )

            val second =
                awaitItem()

            require(
                second is BusesUpdate.Success
            )

            assertEquals(
                41.6435,
                second.buses.first()
                    .position.latitude,
                0.0,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

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