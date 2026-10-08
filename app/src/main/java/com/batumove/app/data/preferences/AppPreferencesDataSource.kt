package com.batumove.app.data.preferences


interface AppPreferencesDataSource {

    suspend fun getLastTransportSync(): Long?

    suspend fun setLastTransportSync(
        timestamp: Long,
    )

    suspend fun getLanguageTag(): String?

    suspend fun setLanguageTag(
        languageTag: String,
    )

    suspend fun isOnboardingCompleted(): Boolean

    suspend fun setOnboardingCompleted(
        completed: Boolean,
    )

    suspend fun getTheme(): String?
    suspend fun setTheme(theme: String)
}