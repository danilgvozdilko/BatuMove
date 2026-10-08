package com.batumove.app.domain.usecase

import com.batumove.app.domain.model.BusPositionSample
import com.batumove.app.domain.model.GeoPoint
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CalculateBusSpeedUseCase @Inject constructor() {

    operator fun invoke(
        samples: List<BusPositionSample>,
    ): Double? {

        if (samples.size < 2) {
            return null
        }

        val sortedSamples =
            samples.sortedBy {
                it.timestampMillis
            }

        var distanceMeters =
            0.0

        for (
            index in
            0 until sortedSamples.lastIndex
        ) {

            distanceMeters +=
                distanceMeters(
                    first =
                        sortedSamples[index].position,
                    second =
                        sortedSamples[index + 1].position,
                )
        }

        val durationMillis =
            sortedSamples.last().timestampMillis -
                sortedSamples.first().timestampMillis

        if (durationMillis <= 0L) {
            return null
        }

        val durationSeconds =
            durationMillis / 1000.0

        return distanceMeters /
            durationSeconds
    }

    private fun distanceMeters(
        first: GeoPoint,
        second: GeoPoint,
    ): Double {

        val earthRadius =
            6_371_000.0

        val lat1 =
            Math.toRadians(
                first.latitude
            )

        val lat2 =
            Math.toRadians(
                second.latitude
            )

        val deltaLat =
            Math.toRadians(
                second.latitude -
                    first.latitude
            )

        val deltaLon =
            Math.toRadians(
                second.longitude -
                    first.longitude
            )

        val a =
            sin(deltaLat / 2) *
                sin(deltaLat / 2) +
                cos(lat1) *
                cos(lat2) *
                sin(deltaLon / 2) *
                sin(deltaLon / 2)

        val c =
            2 * atan2(
                sqrt(a),
                sqrt(1 - a),
            )

        return earthRadius * c
    }
}