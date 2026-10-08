package com.batumove.app.data.mapper

import com.batumove.app.data.remote.dto.BusDto
import com.batumove.app.data.remote.dto.BusStopDto
import com.batumove.app.data.remote.dto.GeoPointDto
import com.batumove.app.data.remote.dto.RouteDto
import com.batumove.app.data.remote.dto.RouteRelationDto
import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.BusStop
import com.batumove.app.domain.model.GeoPoint
import com.batumove.app.domain.model.Route
import com.batumove.app.domain.model.RouteStop

internal fun BusDto.toDomain(
    routeId: String
): Bus =
    Bus(
        name = name,
        routeId = routeId,
        position = GeoPoint(
            latitude = latitude,
            longitude = longitude
        ),
        direction = status.toDirection()
    )


internal fun RouteDto.toDomain(): Route =
    Route(
        id = id,
        number = nameEn,
        isCircular = isCircular,
        sortOrder = sortOrder
    )

internal fun BusStopDto.toDomain(
    id: String,
): BusStop =
    BusStop(
        id = id,
        number = number,
        name = nameEn
            ?.takeIf { it.isNotBlank() }
            ?: nameKa?.takeIf { it.isNotBlank() }
            ?: name?.takeIf { it.isNotBlank() }
            ?: "Unknown stop",
        position = GeoPoint(
            latitude = latitude,
            longitude = longitude
        )
    )

internal fun GeoPointDto.toDomain(): GeoPoint =
    GeoPoint(
        latitude = lat,
        longitude = lon
    )


internal fun BusStopDto.toRouteStop(
    id: String,
    relation: RouteRelationDto,
): RouteStop =
    RouteStop(
        stop = toDomain(id),
        direction = relation.status.toDirection(),
        order = relation.order
    )