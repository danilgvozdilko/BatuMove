package com.batumove.app.data.local.mapper

import com.batumove.app.data.local.entity.RouteEntity
import com.batumove.app.data.local.entity.RouteGeometryPointEntity
import com.batumove.app.data.local.entity.StopEntity
import com.batumove.app.data.local.model.RouteStopWithStop
import com.batumove.app.domain.model.BusStop
import com.batumove.app.domain.model.Direction
import com.batumove.app.domain.model.GeoPoint
import com.batumove.app.domain.model.Route
import com.batumove.app.domain.model.RouteStop

fun RouteEntity.toDomain(): Route =
    Route(
        id = id,
        number = number,
        isCircular = isCircular,
        sortOrder = sortOrder,
    )

fun RouteStopWithStop.toDomain(): RouteStop =
    RouteStop(
        stop = BusStop(
            id = stop.id,
            number = stop.number,
            name = stop.name,
            position = GeoPoint(
                latitude = stop.latitude,
                longitude = stop.longitude,
            ),
        ),
        direction = direction.toDirection(),
        order = order,
    )

private fun Int.toDirection(): Direction =
    when (this) {
        1 -> Direction.OUTBOUND
        2 -> Direction.INBOUND
        else -> Direction.UNKNOWN
    }

fun RouteGeometryPointEntity.toDomain(): GeoPoint =
    GeoPoint(
        latitude = latitude,
        longitude = longitude,
    )

fun StopEntity.toDomain(): BusStop =
    BusStop(
        id = id,
        number = number,
        name = name,
        position = GeoPoint(
            latitude = latitude,
            longitude = longitude,
        ),
    )