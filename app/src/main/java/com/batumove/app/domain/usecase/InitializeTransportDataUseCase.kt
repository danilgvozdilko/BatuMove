package com.batumove.app.domain.usecase

import com.batumove.app.domain.repository.TransportDataRepository
import com.batumove.app.domain.time.TimeProvider
import javax.inject.Inject


private const val SYNC_INTERVAL_MILLIS =
    24 * 60 * 60 * 1000L

class InitializeTransportDataUseCase @Inject constructor(
    private val repository: TransportDataRepository,
    private val timeProvider: TimeProvider,
) {
    suspend operator fun invoke() {

        val hasLocalData =
            repository.hasLocalData()
        if (!hasLocalData) {
            repository.sync()
            return
        }

        val lastSync =
            repository.getLastSyncTime()
        if (lastSync == null) {
            tryRefresh()
            return
        }

        val now =
            timeProvider.currentTimeMillis()
        val dataAge =
            now - lastSync
        if (dataAge >= SYNC_INTERVAL_MILLIS) {
            tryRefresh()
        }
    }

    private suspend fun tryRefresh() {
        try {
            repository.sync()
        } catch (
            exception: kotlinx.coroutines.CancellationException
        ) {

            throw exception

        } catch (e: Exception) {
            print(e.message)
        }
    }
}