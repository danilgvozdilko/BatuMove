package com.batumove.app.domain.model

enum class AppLanguage(
    val languageTag: String,
) {
    ENGLISH("en"),
    GEORGIAN("ka"),
    UKRAINIAN("uk"),
    RUSSIAN("ru"),
    ;

    companion object {
        fun fromLanguageTag(
            languageTag: String,
        ): AppLanguage? =
            entries.firstOrNull {
                it.languageTag == languageTag
            }
    }
}