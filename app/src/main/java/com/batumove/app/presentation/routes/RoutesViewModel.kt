package com.batumove.app.presentation.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batumove.app.domain.usecase.GetRoutesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val getRoutes:
    GetRoutesUseCase,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<RoutesUiState>(
            RoutesUiState.Loading
        )

    val uiState: StateFlow<RoutesUiState> =
        _uiState.asStateFlow()

    init {
        loadRoutes()
    }

    fun retry() {
        loadRoutes()
    }

    private fun loadRoutes() {
        viewModelScope.launch {

            _uiState.value =
                RoutesUiState.Loading

            try {

                val routes =
                    getRoutes()

                _uiState.value =
                    if (routes.isEmpty()) {

                        RoutesUiState.Empty

                    } else {

                        RoutesUiState.Content(
                            routes = routes
                        )
                    }

            } catch (
                exception: CancellationException
            ) {

                throw exception

            } catch (
                exception: Exception
            ) {

                _uiState.value =
                    RoutesUiState.Error
            }
        }
    }
}