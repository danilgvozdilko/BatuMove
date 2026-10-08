package com.batumove.app.domain.repository

import com.batumove.app.domain.model.AppLanguage
import com.batumove.app.domain.model.AppTheme

interface AppPreferencesRepository {

    suspend fun getLanguage(): AppLanguage?

    suspend fun saveLanguage(
        language: AppLanguage,
    )

    suspend fun isOnboardingCompleted(): Boolean

    suspend fun completeOnboarding()

    suspend fun getTheme(): AppTheme
    suspend fun saveTheme(theme: AppTheme)

}