package com.batumove.app.domain.repository

import com.batumove.app.data.preferences.AppPreferencesDataSource
import com.batumove.app.domain.model.AppLanguage
import com.batumove.app.domain.model.AppTheme
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val dataSource:
    AppPreferencesDataSource,
) : AppPreferencesRepository {

    override suspend fun getLanguage():
            AppLanguage? {

        val tag =
            dataSource.getLanguageTag()
                ?: return null

        return AppLanguage.fromLanguageTag(tag)
    }

    override suspend fun saveLanguage(
        language: AppLanguage,
    ) {
        dataSource.setLanguageTag(
            language.languageTag
        )
    }

    override suspend fun isOnboardingCompleted():
            Boolean =
        dataSource.isOnboardingCompleted()

    override suspend fun completeOnboarding() {
        dataSource.setOnboardingCompleted(
            true
        )
    }

    override suspend fun getTheme(): AppTheme {
        val theme = dataSource.getTheme()
            ?: return AppTheme.SYSTEM

        return AppTheme.fromKey(theme)
    }

    override suspend fun saveTheme(
        theme: AppTheme,
    ) {
        dataSource.setTheme(theme.key)
    }
}