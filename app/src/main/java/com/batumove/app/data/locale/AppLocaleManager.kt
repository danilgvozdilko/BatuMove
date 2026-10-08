package com.batumove.app.data.locale

import com.batumove.app.domain.model.AppLanguage

interface AppLocaleManager {

    fun applyLanguage(
        language: AppLanguage,
    )
}