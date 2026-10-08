package com.batumove.app.domain.usecase

import android.location.Location
import com.batumove.app.domain.model.BusStop
import com.batumove.app.domain.model.GeoPoint
import com.batumove.app.domain.model.NearbyStop
import jakarta.inject.Inject

class GetNearbyStopsUseCase @Inject constructor() {

    operator fun invoke(
        userLocation: GeoPoint,
        stops: List<BusStop>,
        radiusMeters: Int = 1_000,
        limit: Int = 15,
    ): List<NearbyStop> {

        return stops
            .asSequence()
            .map { stop ->

                val distance =
                    calculateDistance(
                        from = userLocation,
                        to = stop.position,
                    )

                NearbyStop(
                    stop = stop,
                    distanceMeters = distance,
                )
            }
            .filter { nearbyStop ->
                nearbyStop.distanceMeters <= radiusMeters
            }
            .sortedBy { nearbyStop ->
                nearbyStop.distanceMeters
            }
            .take(limit)
            .toList()
    }

    private fun calculateDistance(
        from: GeoPoint,
        to: GeoPoint,
    ): Int {

        val result = FloatArray(1)

        Location.distanceBetween(
            from.latitude,
            from.longitude,
            to.latitude,
            to.longitude,
            result,
        )

        return result[0].toInt()
    }
}