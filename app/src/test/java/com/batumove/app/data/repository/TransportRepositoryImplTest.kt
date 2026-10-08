package com.batumove.app.data.repository

import com.batumove.app.data.remote.dto.BusDto
import com.batumove.app.data.remote.dto.BusStopDto
import com.batumove.app.data.remote.dto.BusesResponseDto
import com.batumove.app.data.remote.dto.DbDataDto
import com.batumove.app.data.remote.dto.DbResponseDto
import com.batumove.app.data.remote.dto.GeoPointDto
import com.batumove.app.data.remote.dto.RouteDto
import com.batumove.app.data.remote.dto.RouteRelationDto
import com.batumove.app.domain.model.Direction
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.io.IOException

class TransportRepositoryImplTest {

    private lateinit var remoteDataSource: FakeTransportRemoteDataSource
    private lateinit var repository: TransportRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = FakeTransportRemoteDataSource()

        repository = TransportRepositoryImpl(
            remoteDataSource = remoteDataSource,
        )
    }

    @Test
    fun `routes are sorted by sort order`() = runTest {

        remoteDataSource.databaseResponse =
            DbResponseDto(
                data = DbDataDto(
                    busStops = emptyMap(),

                    routesNames = mapOf(
                        "route_10" to RouteDto(
                            id = "route_10",
                            name = "10",
                            nameKa = "10",
                            nameEn = "10",
                            isCircular = false,
                            sortOrder = 100,
                        ),

                        "route_1" to RouteDto(
                            id = "route_1",
                            name = "1",
                            nameKa = "1",
                            nameEn = "1",
                            isCircular = false,
                            sortOrder = 10,
                        ),

                        "route_2" to RouteDto(
                            id = "route_2",
                            name = "2",
                            nameKa = "2",
                            nameEn = "2",
                            isCircular = false,
                            sortOrder = 20,
                        ),
                    ),

                    routeCoordinatesGrouped = emptyMap()
                )
            )

        // Act

        val result = repository.getRoutes()

        // Assert

        assertEquals(
            listOf("1", "2", "10"),
            result.map { it.number },
        )
    }

    @Test
    fun `route stops contain only requested direction and are sorted`() = runTest {

        // Arrange

        val routeId = "route_2"

        remoteDataSource.databaseResponse =
            DbResponseDto(
                data = DbDataDto(

                    routesNames = emptyMap(),

                    routeCoordinatesGrouped = emptyMap(),

                    busStops = mapOf(

                        "stop_3" to createStop(
                            name = "Stop 3",
                            routeId = routeId,
                            status = 1,
                            order = 3,
                        ),

                        "stop_1" to createStop(
                            name = "Stop 1",
                            routeId = routeId,
                            status = 1,
                            order = 1,
                        ),

                        "other_direction" to createStop(
                            name = "Wrong direction",
                            routeId = routeId,
                            status = 2,
                            order = 2,
                        ),

                        "stop_2" to createStop(
                            name = "Stop 2",
                            routeId = routeId,
                            status = 1,
                            order = 2,
                        ),
                    ),
                ),
            )

        // Act

        val result = repository.getRouteStops(
            routeId = routeId,
            direction = Direction.OUTBOUND,
        )

        // Assert

        assertEquals(
            listOf(
                "Stop 1",
                "Stop 2",
                "Stop 3",
            ),
            result.map { it.stop.name },
        )
    }

    @Test
    fun `buses are returned only for requested route`() = runTest {
        // Arrange
        val routeId = "route_2"

        remoteDataSource.busesResponse = BusesResponseDto(
            data = mapOf(
                routeId to listOf(
                    BusDto(
                        latitude = 41.6431433,
                        longitude = 41.655435,
                        status = 1,
                        name = "CN 406 NC",
                    ),
                    BusDto(
                        latitude = 41.6462683,
                        longitude = 41.64902,
                        status = 2,
                        name = "CN 410 NC",
                    ),
                ),
                "route_10" to listOf(
                    BusDto(
                        latitude = 41.650000,
                        longitude = 41.640000,
                        status = 1,
                        name = "OTHER BUS",
                    ),
                ),
            ),
            updatedAt = 123456789L,
        )

        // Act
        val result = repository.getBuses(routeId)

        // Assert
        assertEquals(2, result.size)

        assertEquals(
            listOf("CN 406 NC", "CN 410 NC"),
            result.map { it.name },
        )

        assertEquals(
            listOf(
                Direction.OUTBOUND,
                Direction.INBOUND,
            ),
            result.map { it.direction },
        )

        assertEquals(
            listOf(routeId, routeId),
            result.map { it.routeId },
        )
    }

    @Test
    fun `bus with unsupported status has unknown direction`() = runTest {
        // Arrange
        val routeId = "route_3"

        remoteDataSource.busesResponse = BusesResponseDto(
            data = mapOf(
                routeId to listOf(
                    BusDto(
                        latitude = 41.6282533,
                        longitude = 41.63298,
                        status = -1,
                        name = "TT 683 ET",
                    ),
                ),
            ),
            updatedAt = 123456789L,
        )

        // Act
        val result = repository.getBuses(routeId)

        // Assert
        assertEquals(1, result.size)
        assertEquals(Direction.UNKNOWN, result.first().direction)
    }

    @Test
    fun `missing route in realtime response returns empty bus list`() = runTest {
        // Arrange
        remoteDataSource.busesResponse = BusesResponseDto(
            data = mapOf(
                "route_1" to emptyList(),
            ),
            updatedAt = 123456789L,
        )

        // Act
        val result = repository.getBuses(
            routeId = "route_37",
        )

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `route geometry maps coordinates for requested route`() = runTest {
        // Arrange
        val routeId = "route_2"

        remoteDataSource.databaseResponse = DbResponseDto(
            data = DbDataDto(
                busStops = emptyMap(),
                routesNames = emptyMap(),
                routeCoordinatesGrouped = mapOf(
                    routeId to listOf(
                        GeoPointDto(
                            lat = 41.620772,
                            lon = 41.591956,
                        ),
                        GeoPointDto(
                            lat = 41.620940,
                            lon = 41.598253,
                        ),
                        GeoPointDto(
                            lat = 41.617943,
                            lon = 41.596896,
                        ),
                    ),
                    "route_10" to listOf(
                        GeoPointDto(
                            lat = 42.0,
                            lon = 42.0,
                        ),
                    ),
                ),
            ),
        )

        // Act
        val result = repository.getRouteGeometry(routeId)

        // Assert
        assertEquals(routeId, result.routeId)
        assertEquals(3, result.points.size)

        assertEquals(
            41.620772,
            result.points[0].latitude,
            0.0,
        )

        assertEquals(
            41.591956,
            result.points[0].longitude,
            0.0,
        )
    }

    @Test
    fun `missing route geometry returns empty points`() = runTest {
        // Arrange
        remoteDataSource.databaseResponse = DbResponseDto(
            data = DbDataDto(
                busStops = emptyMap(),
                routesNames = emptyMap(),
                routeCoordinatesGrouped = emptyMap(),
            ),
        )

        // Act
        val result = repository.getRouteGeometry(
            routeId = "unknown_route",
        )

        // Assert
        assertEquals("unknown_route", result.routeId)
        assertTrue(result.points.isEmpty())
    }

    @Test
    fun `network error while loading buses is propagated`() = runTest {
        // Arrange
        val expectedException = IOException("No internet")

        remoteDataSource.busesException = expectedException

        // Act
        try {
            repository.getBuses("route_2")

            fail("Expected IOException")
        } catch (actual: IOException) {

            // Assert
            assertEquals(
                expectedException.message,
                actual.message,
            )
        }
    }

    private fun createStop(
        name: String,
        routeId: String,
        status: Int,
        order: Int,
    ) = BusStopDto(
        number = null,
        name = name,
        nameKa = name,
        nameEn = name,
        latitude = 41.0,
        longitude = 41.0,
        routes = mapOf(
            routeId to
                    RouteRelationDto(
                        status = status,
                        order = order,
                    ),
        ),
    )
}