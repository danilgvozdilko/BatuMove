package com.batumove.app.data.theme

import com.batumove.app.domain.model.AppTheme
import kotlinx.coroutines.flow.StateFlow

interface AppThemeManager {

    val theme: StateFlow<AppTheme>

    fun applyTheme(
        theme: AppTheme,
    )
}