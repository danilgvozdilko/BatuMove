package com.batumove.app.domain.usecase

import com.batumove.app.domain.model.Route
import com.batumove.app.domain.repository.TransportRepository
import javax.inject.Inject

class GetRoutesUseCase @Inject constructor(
    private val repository: TransportRepository,
) {

    suspend operator fun invoke(): List<Route> =
        repository.getRoutes()
}