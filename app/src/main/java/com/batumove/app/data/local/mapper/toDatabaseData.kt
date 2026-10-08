package com.batumove.app.data.local.mapper

import com.batumove.app.data.local.entity.RouteEntity
import com.batumove.app.data.local.entity.RouteGeometryPointEntity
import com.batumove.app.data.local.entity.RouteStopEntity
import com.batumove.app.data.local.entity.StopEntity
import com.batumove.app.data.local.model.TransportDatabaseData
import com.batumove.app.data.remote.dto.DbResponseDto
import com.batumove.app.domain.model.Direction

fun DbResponseDto.toDatabaseData(): TransportDatabaseData {

    val routes = data.routesNames
        .map { (routeId, dto) ->

            RouteEntity(
                id = routeId,
                number = dto.nameEn,
                isCircular = dto.isCircular,
                sortOrder = dto.sortOrder,
            )
        }

    val stops = data.busStops
        .map { (stopId, dto) ->

            StopEntity(
                id = stopId,
                number = dto.number,
                name = resolveStopName(
                    nameEn = dto.nameEn,
                    nameKa = dto.nameKa,
                    fallback = dto.name,
                ),
                latitude = dto.latitude,
                longitude = dto.longitude,
            )
        }

    val routeStops = data.busStops
        .flatMap { (stopId, stopDto) ->

            stopDto.routes.map { (routeId, relation) ->

                RouteStopEntity(
                    routeId = routeId,
                    stopId = stopId,
                    direction = relation.status,
                    order = relation.order,
                )
            }
        }

    val geometry = data.routeCoordinatesGrouped
        .flatMap { (routeId, points) ->

            points.mapIndexed { index, point ->

                RouteGeometryPointEntity(
                    routeId = routeId,
                    pointOrder = index,
                    latitude = point.lat,
                    longitude = point.lon,
                )
            }
        }

    return TransportDatabaseData(
        routes = routes,
        stops = stops,
        routeStops = routeStops,
        geometry = geometry,
    )
}

private fun resolveStopName(
    nameEn: String?,
    nameKa: String?,
    fallback: String?,
): String {

    return nameEn
        ?.takeIf { it.isNotBlank() }
        ?: nameKa?.takeIf { it.isNotBlank() }
        ?: fallback?.takeIf { it.isNotBlank() }
        ?: ""
}

fun Direction.toDatabaseValue(): Int =
    when (this) {
        Direction.OUTBOUND -> 1
        Direction.INBOUND -> 2
        Direction.UNKNOWN -> -1
    }