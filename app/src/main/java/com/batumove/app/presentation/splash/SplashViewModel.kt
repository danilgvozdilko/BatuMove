package com.batumove.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batumove.app.domain.usecase.InitializeTransportDataUseCase
import com.batumove.app.domain.usecase.IsOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val initializeTransportData:
    InitializeTransportDataUseCase,

    private val isOnboardingCompleted:
    IsOnboardingCompletedUseCase,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<SplashUiState>(
            SplashUiState.Loading
        )

    val uiState: StateFlow<SplashUiState> =
        _uiState.asStateFlow()

    init {
        initialize()
    }

    fun retry() {
        if (_uiState.value == SplashUiState.Loading) {
            return
        }

        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {

            _uiState.value =
                SplashUiState.Loading

            try {
                initializeTransportData()

                val onboardingCompleted =
                    isOnboardingCompleted()

                val destination =
                    if (onboardingCompleted) {
                        SplashDestination.Main
                    } else {
                        SplashDestination.Language
                    }

                _uiState.value =
                    SplashUiState.Ready(
                        destination = destination,
                    )

            } catch (
                exception: CancellationException
            ) {
                throw exception

            } catch (
                exception: Exception
            ) {
                _uiState.value =
                    SplashUiState.Error
            }
        }
    }
}