package com.batumove.app.domain.usecase

import com.batumove.app.domain.model.BusesUpdate
import kotlinx.coroutines.flow.Flow

interface ObserveRouteBuses {

    operator fun invoke(
        routeId: String,
    ): Flow<BusesUpdate>
}