package com.batumove.app.domain.usecase

import com.batumove.app.domain.model.BusesUpdate
import com.batumove.app.domain.repository.TransportRepository
import jakarta.inject.Inject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.seconds

class ObserveAllBusesUseCase @Inject constructor(
    private val repository: TransportRepository,
) {

    operator fun invoke(): Flow<BusesUpdate> =
        flow {

            while (
                currentCoroutineContext().isActive
            ) {
                try {

                    emit(
                        BusesUpdate.Success(
                            repository.getBuses()
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
                        BusesUpdate.Error(
                            exception
                        )
                    )
                }

                delay(10.seconds)
            }
        }
}