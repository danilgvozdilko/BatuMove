package com.batumove.app.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AppPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AppPreferencesDataSource {

    override suspend fun getLastTransportSync(): Long? =
        dataStore.data
            .map { preferences ->
                preferences[LAST_TRANSPORT_SYNC]
            }
            .first()

    override suspend fun setLastTransportSync(
        timestamp: Long,
    ) {
        dataStore.edit { preferences ->
            preferences[LAST_TRANSPORT_SYNC] =
                timestamp
        }
    }

    override suspend fun getLanguageTag(): String? =
        dataStore.data
            .map { preferences ->
                preferences[LANGUAGE_TAG]
            }
            .first()

    override suspend fun setLanguageTag(
        languageTag: String,
    ) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_TAG] =
                languageTag
        }
    }

    override suspend fun isOnboardingCompleted(): Boolean =
        dataStore.data
            .map { preferences ->
                preferences[ONBOARDING_COMPLETED]
                    ?: false
            }
            .first()

    override suspend fun setOnboardingCompleted(
        completed: Boolean,
    ) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] =
                completed
        }
    }

    override suspend fun getTheme(): String? {
        return dataStore.data
            .map { preferences ->
                preferences[THEME_KEY]
            }
            .first()
    }

    override suspend fun setTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }


    private companion object {

        val THEME_KEY =
            stringPreferencesKey("theme")

        val LAST_TRANSPORT_SYNC =
            longPreferencesKey(
                "last_transport_sync"
            )

        val LANGUAGE_TAG =
            stringPreferencesKey(
                "language_tag"
            )

        val ONBOARDING_COMPLETED =
            booleanPreferencesKey(
                "onboarding_completed"
            )
    }
}