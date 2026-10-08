package com.batumove.app.presentation.language

import com.batumove.app.domain.model.AppLanguage


data class LanguageUiState(
    val selectedLanguage: AppLanguage? = null,
    val isSaving: Boolean = false,
    val completedLanguage: AppLanguage? = null,
    val hasError: Boolean = false,
)