package com.batumove.app.data.repository

import com.batumove.app.domain.FakeTimeProvider
import com.batumove.app.domain.time.TimeProvider
import com.batumove.app.domain.usecase.InitializeTransportDataUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class InitializeTransportDataUseCaseTest {

    private lateinit var repository:
            FakeTransportDataRepository

    private lateinit var useCase:
            InitializeTransportDataUseCase

    private lateinit var timeProvider: FakeTimeProvider

    @Before
    fun setUp() {
        repository =
            FakeTransportDataRepository()

        timeProvider = FakeTimeProvider()

        useCase =
            InitializeTransportDataUseCase(
                repository, timeProvider
            )
    }

    @Test
    fun `empty database triggers sync`() =
        runTest {

            repository.hasLocalData = false

            useCase()

            assertEquals(
                1,
                repository.syncCalls,
            )
        }

    @Test
    fun `fresh local data does not trigger sync`() =
        runTest {

            val now =
                1_000_000_000L

            repository.hasLocalData = true

            repository.lastSyncTime =
                now - 60 * 60 * 1000L

            timeProvider.currentTime =
                now

            useCase()

            assertEquals(
                0,
                repository.syncCalls,
            )
        }


}