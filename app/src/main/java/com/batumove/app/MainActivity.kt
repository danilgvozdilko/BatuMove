package com.batumove.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.batumove.app.data.theme.AppThemeManager
import com.batumove.app.presentation.navigation.BatuMoveNavHost
import com.batumove.app.ui.theme.BatuMoveTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeManager: AppThemeManager

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val theme by themeManager.theme
                .collectAsStateWithLifecycle()

            BatuMoveTheme(
                theme = theme
            ) {
                val navController =
                    rememberNavController()

                BatuMoveNavHost(
                    navController = navController,
                )
            }
        }
    }
}