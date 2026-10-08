package com.batumove.app.domain.model

enum class AppTheme(
    val key: String,
) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system");

    companion object {
        fun fromKey(key: String): AppTheme {
            return entries.firstOrNull {
                it.key == key
            } ?: SYSTEM
        }
    }
}