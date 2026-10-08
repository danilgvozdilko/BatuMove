package com.batumove.app.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.batumove.app.R
import com.batumove.app.presentation.map.MapRoute
import com.batumove.app.presentation.routes.RoutesRoute
import com.batumove.app.presentation.settings.SettingsRoute
import com.batumove.app.ui.components.BatuMoveBottomBar
import com.batumove.app.ui.components.BatuMoveTopBar

@Composable
fun MainScreen(
    onRouteClick: (String, String?, String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedDestination by
    rememberSaveable {
        mutableStateOf(
            MainDestination.ROUTES
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),

        topBar = {
            BatuMoveTopBar()
        },

        bottomBar = {
            BatuMoveBottomBar(
                selectedDestination =
                    selectedDestination,
                onDestinationSelected = {
                    selectedDestination = it
                },
            )
        },
    ) { innerPadding ->

        MainContent(
            destination =
                selectedDestination,
            onRouteClick =
                onRouteClick,
            modifier = Modifier.padding(
                innerPadding
            ),
        )
    }
}

@Composable
private fun MainContent(
    destination: MainDestination,
    onRouteClick: (String, String?, String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (destination) {

        MainDestination.ROUTES -> {
            RoutesRoute(
                onRouteClick = { routeId ->
                    onRouteClick(
                        routeId,
                        null,
                        null,
                    )
                },
                modifier = modifier,
            )
        }

        MainDestination.MAP -> {
            MapRoute(
                onRouteClick = onRouteClick,
                modifier = modifier,
            )
        }

        MainDestination.SETTINGS -> {
            SettingsRoute(
                modifier = modifier,
            )
        }
    }
}
