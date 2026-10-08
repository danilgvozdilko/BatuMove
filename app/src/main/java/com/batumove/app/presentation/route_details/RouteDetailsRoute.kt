package com.batumove.app.presentation.route_details

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.batumove.app.R
import com.batumove.app.domain.model.Direction
import com.batumove.app.ui.components.BatuMoveTopBar
import com.batumove.app.ui.components.routeDetails.RouteDetailsContent
import com.batumove.app.ui.components.routeDetails.RouteDetailsError

@Composable
fun RouteDetailsRoute(

    routeId: String,

    selectedBusName: String?,

    selectedBusDirection: String?,

    selectedStopId: String?,

    onBackClick: () -> Unit,

    viewModel: RouteDetailsViewModel =
        hiltViewModel(),

    ) {
    val uiState by
    viewModel.uiState
        .collectAsStateWithLifecycle()

    val context =
        LocalContext.current

    val initialDirection =
        remember(
            selectedBusDirection
        ) {
            selectedBusDirection
                ?.let { value ->

                    runCatching {
                        Direction.valueOf(value)
                    }.getOrNull()
                }
                ?: Direction.OUTBOUND
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .RequestMultiplePermissions(),
        ) { permissions ->

            val granted =
                permissions[
                    Manifest.permission
                        .ACCESS_FINE_LOCATION
                ] == true ||
                        permissions[
                            Manifest.permission
                                .ACCESS_COARSE_LOCATION
                        ] == true

            viewModel
                .onLocationPermissionChanged(
                    granted
                )
        }

    LaunchedEffect(
        routeId,
        selectedBusName,
        selectedStopId,
        initialDirection,
    ) {
        viewModel.loadRoute(
            routeId =
                routeId,

            selectedBusName =
                selectedBusName,

            selectedStopId =
                selectedStopId,

            direction =
                initialDirection,
        )
    }

    LaunchedEffect(Unit) {

        val fineGranted =
            ContextCompat
                .checkSelfPermission(
                    context,
                    Manifest.permission
                        .ACCESS_FINE_LOCATION,
                ) ==
                    PackageManager
                        .PERMISSION_GRANTED

        val coarseGranted =
            ContextCompat
                .checkSelfPermission(
                    context,
                    Manifest.permission
                        .ACCESS_COARSE_LOCATION,
                ) ==
                    PackageManager
                        .PERMISSION_GRANTED

        val granted =
            fineGranted ||
                    coarseGranted

        if (granted) {

            viewModel
                .onLocationPermissionChanged(
                    true
                )

        } else {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission
                        .ACCESS_FINE_LOCATION,

                    Manifest.permission
                        .ACCESS_COARSE_LOCATION,
                )
            )
        }
    }

    RouteDetailsScreen(
        uiState = uiState,

        onBackClick =
            onBackClick,

        onDirectionSelected =
            viewModel::changeDirection,

        onStopSelected =
            viewModel::selectStop,

        onRetry =
            viewModel::retry,
    )
}

@Composable
fun RouteDetailsScreen(
    uiState: RouteDetailsUiState,
    onBackClick: () -> Unit,
    onDirectionSelected: (Direction) -> Unit,
    onStopSelected: (String?) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme
                    .colorScheme
                    .background
            ),
    ) {

        RouteDetailsTopBar(
            onBackClick =
                onBackClick,
        )

        when {

            uiState.isLoading -> {

                Box(
                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center,
                ) {

                    CircularProgressIndicator(
                        color =
                            MaterialTheme
                                .colorScheme
                                .primary,
                    )
                }
            }

            uiState.hasError -> {

                RouteDetailsError(
                    onRetry = onRetry,
                )
            }

            else -> {

                RouteDetailsContent(
                    uiState = uiState,

                    onDirectionSelected =
                        onDirectionSelected,

                    onStopSelected =
                        onStopSelected,
                )
            }
        }
    }
}

@Composable
private fun RouteDetailsTopBar(
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp,
            ),

        verticalAlignment =
            Alignment.CenterVertically,
    ) {

        IconButton(
            onClick =
                onBackClick,
        ) {

            Icon(
                imageVector =
                    Icons.AutoMirrored
                        .Rounded
                        .ArrowBack,

                contentDescription =
                    stringResource(
                        R.string.action_back
                    ),
            )
        }

        BatuMoveTopBar(
            modifier =
                Modifier.weight(1f)
        )
    }
}