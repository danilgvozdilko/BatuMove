package com.batumove.app.presentation.main

import androidx.annotation.StringRes
import com.batumove.app.R

enum class MainDestination(
    @StringRes
    val titleRes: Int,
) {

    ROUTES(
        titleRes = R.string.navigation_routes,
    ),

    MAP(
        titleRes = R.string.navigation_map,
    ),

    SETTINGS(
        titleRes = R.string.navigation_settings,
    ),
}