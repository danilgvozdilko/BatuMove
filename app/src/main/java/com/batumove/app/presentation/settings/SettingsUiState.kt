package com.batumove.app.presentation.settings

import com.batumove.app.domain.model.AppLanguage
import com.batumove.app.domain.model.AppTheme

data class SettingsUiState(
    val selectedLanguage: AppLanguage? = null,
    val selectedTheme: AppTheme = AppTheme.LIGHT,
    val isSaving: Boolean = false,
    val hasError: Boolean = false,
)