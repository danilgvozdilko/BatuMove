package com.batumove.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.batumove.app.presentation.language.LanguageRoute
import com.batumove.app.presentation.main.MainScreen
import com.batumove.app.presentation.route_details.RouteDetailsRoute
import com.batumove.app.presentation.settings.SettingsRoute
import com.batumove.app.presentation.splash.SplashRoute

@Composable
fun BatuMoveNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Splash,
    ) {

        composable<AppDestination.Splash> {

            SplashRoute(
                onNavigateToMain = {

                    navController.navigate(
                        AppDestination.Main
                    ) {
                        popUpTo(
                            AppDestination.Splash
                        ) {
                            inclusive = true
                        }
                    }
                },

                onNavigateToLanguage = {

                    navController.navigate(
                        AppDestination.Language
                    ) {
                        popUpTo(
                            AppDestination.Splash
                        ) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable<AppDestination.Language> {

            LanguageRoute(
                onFinished = {

                    navController.navigate(
                        AppDestination.Main
                    ) {
                        popUpTo(
                            AppDestination.Language
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<AppDestination.Settings> {
            SettingsRoute()
        }

        composable<AppDestination.Main> {

            MainScreen(
                onRouteClick = {
                        routeId,
                        selectedBusName,
                        selectedBusDirection ->

                    navController.navigate(
                        AppDestination.RouteDetails(
                            routeId = routeId,
                            selectedBusName =
                                selectedBusName,
                            selectedBusDirection =
                                selectedBusDirection,
                        )
                    )
                }
            )
        }

        composable<AppDestination.RouteDetails> {
                backStackEntry ->

            val destination =
                backStackEntry
                    .toRoute<AppDestination.RouteDetails>()

            RouteDetailsRoute(
                routeId =
                    destination.routeId,

                selectedBusName =
                    destination.selectedBusName,

                selectedBusDirection =
                    destination.selectedBusDirection,

                selectedStopId =
                    destination.selectedStopId,

                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}