package com.batumove.app.data.theme

import com.batumove.app.domain.model.AppTheme
import com.batumove.app.domain.usecase.GetThemeUseCase
import com.batumove.app.domain.usecase.SaveThemeUseCase
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Singleton
class AppThemeManagerImpl @Inject constructor(
    private val getTheme: GetThemeUseCase,
) : AppThemeManager {

    private val _theme =
        MutableStateFlow(AppTheme.SYSTEM)

    override val theme: StateFlow<AppTheme> =
        _theme.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _theme.value = getTheme()
        }
    }

    override fun applyTheme(
        theme: AppTheme,
    ) {
        _theme.value = theme
    }
}