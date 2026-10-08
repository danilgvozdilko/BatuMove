package com.batumove.app.domain.usecase

import android.util.Log
import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.GeoPoint
import com.batumove.app.domain.model.NextBusArrival
import com.batumove.app.domain.model.RouteGeometry
import com.batumove.app.domain.model.RouteStop
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CalculateStopArrivalsUseCase @Inject constructor() {

    operator fun invoke(
        selectedStop: RouteStop,
        routeStops: List<RouteStop>,
        buses: List<Bus>,
        geometry: RouteGeometry,
    ): NextBusArrival? {

        val stops =
            routeStops.sortedBy {
                it.order
            }

        if (
            stops.size < 2 ||
            buses.isEmpty()
        ) {
            return null
        }

        val selectedStopPosition =
            stops.indexOfFirst {
                it.stop.id ==
                        selectedStop.stop.id
            }

        if (selectedStopPosition <= 0) {
            return null
        }

        Log.d(
            TAG,
            """
            ------------------------------
            Selected stop
            name=${selectedStop.stop.name}
            position=$selectedStopPosition
            order=${selectedStop.order}
            buses=${buses.size}
            ------------------------------
            """.trimIndent(),
        )

        val candidates =
            buses.mapNotNull { bus ->

                val segment =
                    findBusStopSegment(
                        bus = bus,
                        stops = stops,
                    )
                        ?: return@mapNotNull null

                if (
                    segment >=
                    selectedStopPosition
                ) {

                    Log.d(
                        TAG,
                        "Bus ${bus.name}: " +
                                "already passed selected stop, " +
                                "segment=$segment",
                    )

                    return@mapNotNull null
                }

                val stopsAway =
                    selectedStopPosition -
                            segment

                val distanceToSelectedStop =
                    calculateDistanceToSelectedStop(
                        bus = bus,
                        busSegment = segment,
                        selectedStopPosition =
                            selectedStopPosition,
                        stops = stops,
                    )

                Log.d(
                    TAG,
                    """
                    Bus candidate
                    bus=${bus.name}
                    segment=$segment
                    segmentFrom=${stops[segment].stop.name}
                    segmentTo=${stops[segment + 1].stop.name}
                    stopsAway=$stopsAway
                    distance=${distanceToSelectedStop.toInt()}m
                    """.trimIndent(),
                )

                BusCandidate(
                    bus = bus,
                    segment = segment,
                    stopsAway = stopsAway,
                    distanceMeters =
                        distanceToSelectedStop,
                )
            }

        if (candidates.isEmpty()) {

            Log.d(
                TAG,
                "No approaching buses",
            )

            return null
        }

        val nearestBus =
            candidates.minWithOrNull(
                compareBy<BusCandidate> {
                    it.stopsAway
                }.thenBy {
                    it.distanceMeters
                }
            )
                ?: return null

        Log.d(
            TAG,
            """
            ==============================
            NEXT BUS
            bus=${nearestBus.bus.name}
            segment=${nearestBus.segment}
            segmentFrom=${stops[nearestBus.segment].stop.name}
            segmentTo=${stops[nearestBus.segment + 1].stop.name}
            stopsAway=${nearestBus.stopsAway}
            distance=${nearestBus.distanceMeters.toInt()}m
            ==============================
            """.trimIndent(),
        )

        return NextBusArrival(
            bus = nearestBus.bus,
            distanceMeters =
                nearestBus
                    .distanceMeters
                    .toInt()
                    .coerceAtLeast(0),
            stopsAway =
                nearestBus.stopsAway,
        )
    }

    private fun findBusStopSegment(
        bus: Bus,
        stops: List<RouteStop>,
    ): Int? {

        var bestSegment: Int? =
            null

        var bestDistance =
            Double.MAX_VALUE

        for (
        segmentIndex in
        0 until stops.lastIndex
        ) {

            val start =
                stops[
                    segmentIndex
                ].stop.position

            val end =
                stops[
                    segmentIndex + 1
                ].stop.position

            val distance =
                distanceToSegmentMeters(
                    point =
                        bus.position,
                    start =
                        start,
                    end =
                        end,
                )

            if (
                distance <
                bestDistance
            ) {

                bestDistance =
                    distance

                bestSegment =
                    segmentIndex
            }
        }

        if (bestSegment != null) {

            Log.d(
                TAG,
                """
                Bus position
                bus=${bus.name}
                segment=$bestSegment
                from=${stops[bestSegment].stop.name}
                to=${stops[bestSegment + 1].stop.name}
                distanceToSegment=${bestDistance.toInt()}m
                """.trimIndent(),
            )
        }

        return bestSegment
    }

    private fun calculateDistanceToSelectedStop(
        bus: Bus,
        busSegment: Int,
        selectedStopPosition: Int,
        stops: List<RouteStop>,
    ): Double {

        if (
            busSegment >=
            selectedStopPosition
        ) {
            return 0.0
        }

        var distance =
            distanceMeters(
                first =
                    bus.position,
                second =
                    stops[
                        busSegment + 1
                    ].stop.position,
            )

        for (
        stopPosition in
        busSegment + 1 until
                selectedStopPosition
        ) {

            distance +=
                distanceMeters(
                    first =
                        stops[
                            stopPosition
                        ].stop.position,

                    second =
                        stops[
                            stopPosition + 1
                        ].stop.position,
                )
        }

        return distance
    }

    private fun distanceToSegmentMeters(
        point: GeoPoint,
        start: GeoPoint,
        end: GeoPoint,
    ): Double {

        val referenceLatitude =
            Math.toRadians(
                (
                        start.latitude +
                                end.latitude
                        ) / 2.0
            )

        val metersPerDegreeLatitude =
            111_320.0

        val metersPerDegreeLongitude =
            111_320.0 *
                    cos(
                        referenceLatitude
                    )

        val pointX =
            point.longitude *
                    metersPerDegreeLongitude

        val pointY =
            point.latitude *
                    metersPerDegreeLatitude

        val startX =
            start.longitude *
                    metersPerDegreeLongitude

        val startY =
            start.latitude *
                    metersPerDegreeLatitude

        val endX =
            end.longitude *
                    metersPerDegreeLongitude

        val endY =
            end.latitude *
                    metersPerDegreeLatitude

        val segmentX =
            endX -
                    startX

        val segmentY =
            endY -
                    startY

        val segmentLengthSquared =
            segmentX *
                    segmentX +
                    segmentY *
                    segmentY

        if (
            segmentLengthSquared ==
            0.0
        ) {

            return distanceMeters(
                first = point,
                second = start,
            )
        }

        val projection =
            (
                    (
                            pointX -
                                    startX
                            ) *
                            segmentX +
                            (
                                    pointY -
                                            startY
                                    ) *
                            segmentY
                    ) /
                    segmentLengthSquared

        val clampedProjection =
            projection.coerceIn(
                minimumValue =
                    0.0,
                maximumValue =
                    1.0,
            )

        val nearestX =
            startX +
                    clampedProjection *
                    segmentX

        val nearestY =
            startY +
                    clampedProjection *
                    segmentY

        val dx =
            pointX -
                    nearestX

        val dy =
            pointY -
                    nearestY

        return sqrt(
            dx * dx +
                    dy * dy
        )
    }

    private fun distanceMeters(
        first: GeoPoint,
        second: GeoPoint,
    ): Double {

        val earthRadiusMeters =
            6_371_000.0

        val latitude1 =
            Math.toRadians(
                first.latitude
            )

        val latitude2 =
            Math.toRadians(
                second.latitude
            )

        val latitudeDifference =
            Math.toRadians(
                second.latitude -
                        first.latitude
            )

        val longitudeDifference =
            Math.toRadians(
                second.longitude -
                        first.longitude
            )

        val sinLatitude =
            sin(
                latitudeDifference /
                        2.0
            )

        val sinLongitude =
            sin(
                longitudeDifference /
                        2.0
            )

        val a =
            sinLatitude *
                    sinLatitude +
                    cos(latitude1) *
                    cos(latitude2) *
                    sinLongitude *
                    sinLongitude

        val c =
            2.0 *
                    atan2(
                        sqrt(a),
                        sqrt(
                            1.0 - a
                        ),
                    )

        return earthRadiusMeters *
                c
    }

    private data class BusCandidate(
        val bus: Bus,
        val segment: Int,
        val stopsAway: Int,
        val distanceMeters: Double,
    )

    private companion object {

        const val TAG =
            "NextBusArrival"
    }
}