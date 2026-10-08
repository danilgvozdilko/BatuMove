package com.batumove.app.domain.usecase

import com.batumove.app.domain.model.BusesUpdate
import com.batumove.app.domain.repository.TransportRepository
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


class ObserveRouteBusesUseCase @Inject constructor(
    private val repository: TransportRepository,
) : ObserveRouteBuses {

    override operator fun invoke(
        routeId: String,
    ): Flow<BusesUpdate> =
        createPollingFlow(routeId)

    private fun createPollingFlow(
        routeId: String,
    ): Flow<BusesUpdate> =
        flow {
            while (currentCoroutineContext().isActive) {

                try {
                    emit(
                        BusesUpdate.Success(
                            repository.getBuses(routeId)
                        )
                    )
                } catch (
                    exception: CancellationException
                ) {
                    throw exception
                } catch (
                    exception: Exception
                ) {
                    emit(
                        BusesUpdate.Error(exception)
                    )
                }

                delay(10.seconds)
            }
        }
}