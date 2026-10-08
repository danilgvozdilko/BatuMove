package com.batumove.app.presentation.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batumove.app.data.locale.AppLocaleManager
import com.batumove.app.domain.model.AppLanguage
import com.batumove.app.domain.usecase.CompleteOnboardingUseCase
import com.batumove.app.domain.usecase.SaveLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val saveLanguage:
    SaveLanguageUseCase,
    private val completeOnboarding:
    CompleteOnboardingUseCase,
    private val localeManager:
    AppLocaleManager,
) : ViewModel() {


    private val _uiState =
        MutableStateFlow(
            LanguageUiState()
        )

    val uiState: StateFlow<LanguageUiState> =
        _uiState.asStateFlow()

    fun selectLanguage(
        language: AppLanguage,
    ) {
        if (_uiState.value.isSaving) {
            return
        }

        _uiState.update {
            it.copy(
                selectedLanguage = language,
                hasError = false,
            )
        }
    }

    fun continueWithSelectedLanguage() {

        val language =
            _uiState.value.selectedLanguage
                ?: return

        if (_uiState.value.isSaving) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSaving = true,
                    hasError = false,
                )
            }

            try {

                saveLanguage(language)

                localeManager.applyLanguage(
                    language
                )

                completeOnboarding()

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        completedLanguage = language,
                    )
                }

            } catch (
                exception: CancellationException
            ) {

                throw exception

            } catch (
                exception: Exception
            ) {

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        hasError = true,
                    )
                }
            }
        }
    }
}