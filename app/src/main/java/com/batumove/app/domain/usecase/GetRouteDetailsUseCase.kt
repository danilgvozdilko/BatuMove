package com.batumove.app.domain.usecase

import com.batumove.app.domain.model.Direction
import com.batumove.app.domain.model.RouteDetails
import com.batumove.app.domain.repository.TransportRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetRouteDetailsUseCase @Inject constructor(
    private val repository: TransportRepository,
) {

    suspend operator fun invoke(
        routeId: String,
        direction: Direction,
    ): RouteDetails =
        coroutineScope {

            val routeDeferred =
                async {
                    repository.getRoute(
                        routeId = routeId,
                    )
                }

            val stopsDeferred =
                async {
                    repository.getRouteStops(
                        routeId = routeId,
                        direction = direction,
                    )
                }

            val geometryDeferred =
                async {
                    repository.getRouteGeometry(
                        routeId = routeId,
                    )
                }

            RouteDetails(
                route = routeDeferred.await(),
                stops = stopsDeferred.await(),
                geometry = geometryDeferred.await(),
            )
        }
}