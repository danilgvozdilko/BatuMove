package com.batumove.app.presentation.route

import app.cash.turbine.test
import com.batumove.app.domain.model.Route
import com.batumove.app.domain.usecase.GetRoutesUseCase
import com.batumove.app.presentation.routes.RoutesUiState
import com.batumove.app.presentation.routes.RoutesViewModel
import com.batumove.app.usecase.FakeTransportRepository
import com.batumove.app.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RoutesViewModelTest {

    @get:Rule
    val mainDispatcherRule =
        MainDispatcherRule()

    private lateinit var repository: FakeTransportRepository

    private lateinit var useCase: GetRoutesUseCase

    @Before
    fun setUp() {
        repository = FakeTransportRepository()

        useCase = GetRoutesUseCase(repository)
    }

    @Test
    fun `routes are loaded successfully`() =
        runTest {

            // Arrange

            repository.routes = listOf(
                Route(
                    id = "route_1",
                    number = "1",
                    isCircular = false,
                    sortOrder = 10,
                ),
                Route(
                    id = "route_2",
                    number = "2",
                    isCircular = false,
                    sortOrder = 20,
                ),
            )

            // Act

            val viewModel =
                RoutesViewModel(
                    getRoutesUseCase = useCase,
                )

            advanceUntilIdle()

            // Assert

            val state =
                viewModel.uiState.value

            require(
                state is RoutesUiState.Content
            )

            assertEquals(
                listOf("1", "2"),
                state.routes.map {
                    it.number
                },
            )
        }

    @Test
    fun `error state is emitted when routes loading fails`() =
        runTest {

            // Arrange

            repository.routesException =
                IllegalStateException(
                    "Server unavailable"
                )

            // Act

            val viewModel =
                RoutesViewModel(
                    getRoutesUseCase = useCase,
                )

            advanceUntilIdle()

            // Assert

            val state =
                viewModel.uiState.value

            require(
                state is RoutesUiState.Error
            )

            assertEquals(
                "Server unavailable",
                state.message,
            )
        }

    @Test
    fun `ui state changes from loading to content`() =
        runTest {

            // Arrange

            repository.routes = listOf(
                Route(
                    id = "route_2",
                    number = "2",
                    isCircular = false,
                    sortOrder = 20,
                ),
            )

            val viewModel =
                RoutesViewModel(
                    getRoutesUseCase = useCase,
                )

            // Act + Assert

            viewModel.uiState.test {

                val loading =
                    awaitItem()

                assertEquals(
                    RoutesUiState.Loading,
                    loading,
                )

                advanceUntilIdle()

                val content =
                    awaitItem()

                require(
                    content is RoutesUiState.Content
                )

                assertEquals(
                    "2",
                    content.routes.first().number,
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
}