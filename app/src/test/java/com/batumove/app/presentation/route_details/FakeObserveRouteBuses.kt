package com.batumove.app.presentation.route_details


import com.batumove.app.domain.model.BusesUpdate
import com.batumove.app.domain.usecase.ObserveRouteBuses
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeObserveRouteBuses : ObserveRouteBuses {

    private val updates =
        MutableSharedFlow<BusesUpdate>(
            extraBufferCapacity = 1,
        )

    override fun invoke(
        routeId: String,
    ): Flow<BusesUpdate> =
        updates

    suspend fun emit(
        update: BusesUpdate,
    ) {
        updates.emit(update)
    }
}