package com.batumove.app.domain.usecase

import com.batumove.app.domain.model.BusPositionSample
import javax.inject.Inject
import kotlin.math.ceil

class CalculateBusEtaUseCase @Inject constructor(
    private val calculateBusSpeedUseCase:
        CalculateBusSpeedUseCase,
) {

    operator fun invoke(
        distanceMeters: Int,
        samples: List<BusPositionSample>,
    ): Int? {

        val speedMetersPerSecond =
            calculateBusSpeedUseCase(
                samples
            )
                ?: return null

        if (
            speedMetersPerSecond <
            MIN_VALID_SPEED_METERS_PER_SECOND
        ) {
            return null
        }

        val seconds =
            distanceMeters /
                speedMetersPerSecond

        return maxOf(
            1,
            ceil(
                seconds / 60.0
            ).toInt(),
        )
    }

    private companion object {

        const val MIN_VALID_SPEED_METERS_PER_SECOND =
            1.0
    }
}