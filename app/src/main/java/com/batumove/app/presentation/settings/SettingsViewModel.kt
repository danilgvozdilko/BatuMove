package com.batumove.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batumove.app.data.locale.AppLocaleManager
import com.batumove.app.data.theme.AppThemeManager
import com.batumove.app.domain.model.AppLanguage
import com.batumove.app.domain.model.AppTheme
import com.batumove.app.domain.usecase.GetLanguageUseCase
import com.batumove.app.domain.usecase.GetThemeUseCase
import com.batumove.app.domain.usecase.SaveLanguageUseCase
import com.batumove.app.domain.usecase.SaveThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getLanguage: GetLanguageUseCase,
    private val saveLanguage: SaveLanguageUseCase,
    private val getTheme: GetThemeUseCase,
    private val saveTheme: SaveThemeUseCase,
    private val localeManager: AppLocaleManager,
    private val themeManager: AppThemeManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState()
    )

    val uiState: StateFlow<SettingsUiState> =
        _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {

            val language = getLanguage()
            val theme = getTheme()

            _uiState.update {
                it.copy(
                    selectedLanguage = language,
                    selectedTheme = theme,
                )
            }
        }
    }

    fun selectLanguage(
        language: AppLanguage,
    ) {
        if (_uiState.value.isSaving) {
            return
        }

        if (_uiState.value.selectedLanguage == language) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    selectedLanguage = language,
                    isSaving = true,
                    hasError = false,
                )
            }

            try {

                saveLanguage(language)

                localeManager.applyLanguage(language)

                _uiState.update {
                    it.copy(
                        isSaving = false
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

    fun selectTheme(
        theme: AppTheme,
    ) {
        if (_uiState.value.isSaving) {
            return
        }

        if (_uiState.value.selectedTheme == theme) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    selectedTheme = theme,
                    isSaving = true,
                    hasError = false,
                )
            }

            try {

                saveTheme(theme)

                themeManager.applyTheme(theme)

                _uiState.update {
                    it.copy(
                        isSaving = false,
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