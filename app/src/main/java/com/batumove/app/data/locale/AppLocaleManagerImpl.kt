package com.batumove.app.data.locale

import android.app.LocaleManager
import android.content.Context
import android.os.LocaleList
import com.batumove.app.domain.model.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class AppLocaleManagerImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
) : AppLocaleManager {

    override fun applyLanguage(
        language: AppLanguage,
    ) {

        val localeManager =
            context.getSystemService(
                LocaleManager::class.java
            )

        localeManager.applicationLocales =
            LocaleList.forLanguageTags(
                language.languageTag
            )

    }
}