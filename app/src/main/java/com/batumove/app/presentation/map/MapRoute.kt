package com.batumove.app.presentation.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.batumove.app.R
import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.BusStop
import com.batumove.app.domain.model.NearbyStop
import com.batumove.app.domain.model.TripOption
import com.batumove.app.ui.components.BusMapMarker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import java.util.Locale
import kotlin.collections.isNotEmpty
import kotlin.collections.map

@Composable
fun MapRoute(
    onRouteClick: (
        routeId: String,
        selectedBusName: String?,
        selectedBusDirection: String?,
    ) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
) {
    val uiState by
    viewModel.uiState
        .collectAsStateWithLifecycle()

    val context = LocalContext.current

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

    LaunchedEffect(Unit) {

        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED

        val locationGranted =
            fineLocationGranted ||
                    coarseLocationGranted

        if (locationGranted) {

            viewModel
                .onLocationPermissionChanged(true)

        } else {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
        }
    }

    MapScreen(
        uiState = uiState,

        onStopClick =
            viewModel::selectStop,

        onBusClick =
            viewModel::selectBus,

        onRouteClick =
            onRouteClick,

        onRetry =
            viewModel::retry,

        onMyLocationClick = {

            if (
                uiState
                    .locationPermissionGranted
            ) {
                viewModel.refreshLocation()

            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    )
                )
            }
        },

        modifier = modifier,
        onDestinationQueryChanged =
            viewModel::onDestinationQueryChanged,

        onDestinationSelected =
            viewModel::selectDestination,
    )
}


private val Bus.mapKey: String
    get() = "$routeId:$name"

@Composable
fun MapScreen(
    uiState: MapUiState,
    onStopClick: (String?) -> Unit,
    onBusClick: (String?) -> Unit,

    onDestinationQueryChanged: (String) -> Unit,
    onDestinationSelected: (BusStop) -> Unit,

    onRouteClick: (
        routeId: String,
        selectedBusName: String?,
        selectedBusDirection: String?,
    ) -> Unit,

    onRetry: () -> Unit,
    onMyLocationClick: () -> Unit,

    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> {
            MapLoading(
                modifier = modifier,
            )
        }

        uiState.hasError -> {
            MapError(
                onRetry = onRetry,
                modifier = modifier,
            )
        }

        else -> {
            MapContent(
                uiState = uiState,
                onStopClick = onStopClick,
                onBusClick = onBusClick,
                onDestinationQueryChanged =
                    onDestinationQueryChanged,
                onDestinationSelected =
                    onDestinationSelected,
                onRouteClick = onRouteClick,
                onMyLocationClick =
                    onMyLocationClick,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun MapLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color =
                MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun MapError(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center,
            modifier =
                Modifier.padding(32.dp),
        ) {
            Surface(
                shape = CircleShape,
                color =
                    MaterialTheme
                        .colorScheme
                        .errorContainer,
                modifier =
                    Modifier.size(64.dp),
            ) {
                Box(
                    contentAlignment =
                        Alignment.Center,
                ) {
                    Icon(
                        imageVector =
                            Icons.Default.Refresh,
                        contentDescription = null,
                        tint =
                            MaterialTheme
                                .colorScheme
                                .onErrorContainer,
                        modifier =
                            Modifier.size(28.dp),
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp),
            )

            Text(
                text = stringResource(R.string.map_load_error_title),
                style =
                    MaterialTheme
                        .typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold,
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp),
            )

            Text(
                text = stringResource(R.string.map_load_error_subtitle),
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant,
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp),
            )

            Button(
                onClick = onRetry,
                shape =
                    RoundedCornerShape(14.dp),
            ) {
                Text(
                    text = stringResource(R.string.action_retry),
                )
            }
        }
    }
}

@Composable
private fun MapContent(
    uiState: MapUiState,
    onStopClick: (String?) -> Unit,
    onBusClick: (String?) -> Unit,

    onDestinationQueryChanged: (String) -> Unit,
    onDestinationSelected: (BusStop) -> Unit,

    onRouteClick: (
        routeId: String,
        selectedBusName: String?,
        selectedBusDirection: String?,
    ) -> Unit,

    onMyLocationClick: () -> Unit,

    modifier: Modifier = Modifier,
) {
    val defaultLocation =
        remember {
            LatLng(
                41.6461,
                41.6405,
            )
        }

    val cameraPositionState =
        rememberCameraPositionState {
            position =
                CameraPosition.fromLatLngZoom(
                    defaultLocation,
                    13f,
                )
        }

    val mapUiSettings =
        remember {
            MapUiSettings(
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true,
                scrollGesturesEnabled = true,
                rotationGesturesEnabled = true,
                tiltGesturesEnabled = true,
                mapToolbarEnabled = false,
                compassEnabled = false,
                myLocationButtonEnabled = false,
            )
        }

    val mapProperties =
        remember(
            uiState.locationPermissionGranted
        ) {
            MapProperties(
                isMyLocationEnabled =
                    uiState
                        .locationPermissionGranted,
            )
        }

    val routeNumbers =
        remember(uiState.routes) {
            uiState.routes.associate {
                it.id to it.number
            }
        }

    /*
     * Маршруты, которыми можно доехать
     * до выбранной остановки.
     */
    val tripRouteIds =
        remember(uiState.tripOptions) {
            uiState.tripOptions
                .map { option ->
                    option.route.id
                }
                .toSet()
        }

    /*
     * Остановки.
     *
     * Обычный режим:
     * ближайшие к пользователю.
     *
     * Trip mode:
     * только те стартовые остановки,
     * откуда можно уехать к destination.
     */
    val visibleStops =
        remember(
            uiState.nearbyStops,
            uiState.destinationStop,
            uiState.tripOptions,
        ) {
            if (uiState.destinationStop == null) {

                uiState.nearbyStops
                    .map { nearbyStop ->
                        nearbyStop.stop
                    }

            } else {

                uiState.tripOptions
                    .map { option ->
                        option.startStop
                    }
                    .distinctBy { stop ->
                        stop.id
                    }
            }
        }

    /*
     * Автобусы.
     *
     * Обычный режим:
     * только ближайшие.
     *
     * Trip mode:
     * только автобусы маршрутов,
     * которыми можно добраться.
     */
    val visibleBuses =
        remember(
            uiState.destinationStop,
            uiState.nearbyBuses,
            uiState.buses,
            tripRouteIds,
        ) {
            if (uiState.destinationStop == null) {

                uiState.nearbyBuses

            } else {

                uiState.buses.filter { bus ->
                    bus.routeId in tripRouteIds
                }
            }
        }

    val selectedBus =
        remember(
            visibleBuses,
            uiState.selectedBusKey,
        ) {
            visibleBuses
                .firstOrNull { bus ->
                    bus.mapKey ==
                            uiState.selectedBusKey
                }
        }

    val selectedStop =
        remember(
            uiState.nearbyStops,
            uiState.selectedStopId,
        ) {
            uiState.nearbyStops
                .firstOrNull { nearbyStop ->
                    nearbyStop.stop.id ==
                            uiState.selectedStopId
                }
        }

    /*
     * Управление камерой.
     *
     * Если destination выбран —
     * показываем пользователя и destination
     * одновременно.
     *
     * Иначе камера идёт к пользователю.
     */
    LaunchedEffect(
        uiState.userLocation,
        uiState.destinationStop,
    ) {
        val userLocation =
            uiState.userLocation
                ?: return@LaunchedEffect

        val destination =
            uiState.destinationStop

        if (destination != null) {

            val bounds =
                LatLngBounds
                    .builder()
                    .include(
                        LatLng(
                            userLocation.latitude,
                            userLocation.longitude,
                        )
                    )
                    .include(
                        LatLng(
                            destination
                                .position
                                .latitude,
                            destination
                                .position
                                .longitude,
                        )
                    )
                    .build()

            cameraPositionState.animate(
                update =
                    CameraUpdateFactory
                        .newLatLngBounds(
                            bounds,
                            140,
                        ),
                durationMs = 850,
            )

        } else {

            cameraPositionState.animate(
                update =
                    CameraUpdateFactory
                        .newLatLngZoom(
                            LatLng(
                                userLocation.latitude,
                                userLocation.longitude,
                            ),
                            15.5f,
                        ),
                durationMs = 850,
            )
        }
    }

    Box(
        modifier =
            modifier.fillMaxSize(),
    ) {

        /*
         * ВАЖНО:
         * GoogleMap идёт первым,
         * весь UI после неё будет поверх карты.
         */
        GoogleMap(
            modifier =
                Modifier.fillMaxSize(),
            cameraPositionState =
                cameraPositionState,
            uiSettings =
                mapUiSettings,
            properties =
                mapProperties,
            onMapClick = {
                onStopClick(null)
                onBusClick(null)
            },
        ) {

            /*
             * Остановки.
             */
            visibleStops.forEach { stop ->

                val selected =
                    stop.id ==
                            uiState.selectedStopId

                MarkerComposable(
                    state =
                        rememberUpdatedMarkerState(
                            position =
                                LatLng(
                                    stop
                                        .position
                                        .latitude,
                                    stop
                                        .position
                                        .longitude,
                                )
                        ),
                    zIndex =
                        if (selected) {
                            6f
                        } else {
                            2f
                        },
                    onClick = {
                        onStopClick(stop.id)
                        true
                    },
                ) {
                    StopMapMarker(
                        selected = selected,
                    )
                }
            }

            /*
             * Автобусы.
             */
            visibleBuses.forEach { bus ->

                val key =
                    bus.mapKey

                val selected =
                    key ==
                            uiState.selectedBusKey

                val routeNumber =
                    routeNumbers[
                        bus.routeId
                    ] ?: "?"

                MarkerComposable(
                    state =
                        rememberUpdatedMarkerState(
                            position =
                                LatLng(
                                    bus
                                        .position
                                        .latitude,
                                    bus
                                        .position
                                        .longitude,
                                )
                        ),
                    zIndex =
                        if (selected) {
                            15f
                        } else {
                            8f
                        },
                    onClick = {
                        onBusClick(key)
                        true
                    },
                ) {
                    NearbyBusMarker(
                        routeNumber =
                            routeNumber,
                        selected =
                            selected,
                    )
                }
            }

            /*
             * Destination.
             */
            uiState
                .destinationStop
                ?.let { destination ->

                    MarkerComposable(
                        state =
                            rememberUpdatedMarkerState(
                                position =
                                    LatLng(
                                        destination
                                            .position
                                            .latitude,
                                        destination
                                            .position
                                            .longitude,
                                    )
                            ),
                        zIndex = 30f,
                    ) {
                        DestinationMarker()
                    }
                }
        }

        /*
         * Поиск.
         */
        DestinationSearch(
            query =
                uiState.destinationQuery,
            suggestions =
                uiState.destinationSuggestions,
            onQueryChanged =
                onDestinationQueryChanged,
            onStopSelected =
                onDestinationSelected,
            modifier =
                Modifier
                    .align(
                        Alignment.TopCenter
                    )
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                    ),
        )

        /*
         * Не показываем status chip,
         * пока пользователь выбирает
         * suggestion.
         */
        if (
            uiState
                .destinationSuggestions
                .isEmpty()
        ) {
            MapStatusChip(
                destinationStop =
                    uiState.destinationStop,
                tripOptions =
                    uiState.tripOptions,
                buses =
                    visibleBuses.size,
                stops =
                    visibleStops.size,
                hasLocation =
                    uiState.userLocation != null,
                modifier =
                    Modifier
                        .align(
                            Alignment.TopStart
                        )
                        .padding(
                            start = 16.dp,
                            top = 88.dp,
                        ),
            )
        }

        /*
         * Location button.
         */
        FloatingActionButton(
            onClick =
                onMyLocationClick,
            shape =
                CircleShape,
            containerColor =
                MaterialTheme
                    .colorScheme
                    .surface,
            contentColor =
                MaterialTheme
                    .colorScheme
                    .primary,
            modifier =
                Modifier
                    .align(
                        Alignment.BottomEnd
                    )
                    .padding(
                        end = 16.dp,
                        bottom =
                            if (
                                uiState.destinationStop != null ||
                                selectedStop != null ||
                                selectedBus != null
                            ) {
                                180.dp
                            } else {
                                250.dp
                            },
                    ),
        ) {
            Icon(
                imageVector =
                    Icons.Default.MyLocation,
                contentDescription = stringResource(R.string.map_my_location),
            )
        }

        /*
         * Только одна нижняя карточка
         * одновременно.
         */
        when {

            uiState.destinationStop != null -> {

                TripOptionsCard(
                    destinationStop =
                        uiState.destinationStop,
                    options =
                        uiState.tripOptions,
                    isLoading =
                        uiState.isSearchingTrip,
                    onRouteClick =
                        onRouteClick,
                    modifier =
                        Modifier
                            .align(
                                Alignment.BottomCenter
                            )
                            .padding(16.dp),
                )
            }

            selectedStop != null -> {

                SelectedStopCard(
                    nearbyStop =
                        selectedStop,
                    modifier =
                        Modifier
                            .align(
                                Alignment.BottomCenter
                            )
                            .padding(16.dp),
                )
            }

            selectedBus != null -> {

                SelectedBusCard(
                    bus =
                        selectedBus,
                    routeNumber =
                        routeNumbers[
                            selectedBus.routeId
                        ] ?: "?",
                    onRouteClick = {
                        onRouteClick(
                            selectedBus.routeId,
                            selectedBus.name,
                            selectedBus
                                .direction
                                .name,
                        )
                    },
                    modifier =
                        Modifier
                            .align(
                                Alignment.BottomCenter
                            )
                            .padding(16.dp),
                )
            }


            else -> {
                NearbyStopsCard(
                    stops = uiState.nearbyStops.take(3),
                    onStopClick = onStopClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun DestinationSearch(
    query: String,
    suggestions: List<BusStop>,
    onQueryChanged: (String) -> Unit,
    onStopSelected: (BusStop) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {

        OutlinedTextField(
            value = query,
            onValueChange =
                onQueryChanged,
            modifier =
                Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(R.string.map_search_destination),
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector =
                        Icons.Default.Search,
                    contentDescription = null,
                )
            },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(
                        onClick = { onQueryChanged("") },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.clear_search),
                        )
                    }
                }
            },
            shape =
                RoundedCornerShape(18.dp),
        )

        if (suggestions.isNotEmpty()) {

            Surface(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 6.dp
                        ),
                shape =
                    RoundedCornerShape(18.dp),
                color =
                    MaterialTheme
                        .colorScheme
                        .surface,
                shadowElevation =
                    8.dp,
            ) {
                Column {

                    suggestions.forEach { stop ->

                        Surface(
                            onClick = {
                                onStopSelected(stop)
                            },
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .surface,
                        ) {
                            Text(
                                text =
                                    stop.name,
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodyLarge,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal =
                                                18.dp,
                                            vertical =
                                                14.dp,
                                        ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StopMapMarker(
    selected: Boolean,
) {
    Surface(
        shape =
            CircleShape,
        color =
            if (selected) {
                MaterialTheme
                    .colorScheme
                    .primary
            } else {
                MaterialTheme
                    .colorScheme
                    .surface
            },
        border =
            if (selected) {
                null
            } else {
                BorderStroke(
                    width = 2.dp,
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary,
                )
            },
        shadowElevation =
            if (selected) {
                8.dp
            } else {
                4.dp
            },
        modifier =
            Modifier.size(
                if (selected) {
                    34.dp
                } else {
                    28.dp
                }
            ),
    ) {
        Box(
            contentAlignment =
                Alignment.Center,
        ) {
            Icon(
                imageVector =
                    Icons.Default.LocationOn,
                contentDescription = null,
                tint =
                    if (selected) {
                        MaterialTheme
                            .colorScheme
                            .onPrimary
                    } else {
                        MaterialTheme
                            .colorScheme
                            .primary
                    },
                modifier =
                    Modifier.size(
                        if (selected) {
                            18.dp
                        } else {
                            15.dp
                        }
                    ),
            )
        }
    }
}

/*
 * Здесь можешь вместо содержимого использовать
 * свой уже существующий BusMapMarker.
 *
 * Сейчас номер маршрута маленький и вынесен вверх,
 * чтобы не перекрывать автобус.
 */
@Composable
private fun NearbyBusMarker(
    routeNumber: String,
    selected: Boolean,
) {
    Box(
        contentAlignment =
            Alignment.Center,
    ) {

        BusMapMarker(
            selected = selected,
        )

        Surface(
            modifier =
                Modifier
                    .align(
                        Alignment.TopEnd
                    )
                    .offset(
                        x = 5.dp,
                        y = (-5).dp,
                    ),
            shape =
                CircleShape,
            color =
                MaterialTheme
                    .colorScheme
                    .primary,
            shadowElevation =
                2.dp,
        ) {
            Text(
                text =
                    routeNumber,
                color =
                    MaterialTheme
                        .colorScheme
                        .onPrimary,
                fontSize =
                    8.sp,
                lineHeight =
                    8.sp,
                fontWeight =
                    FontWeight.Bold,
                modifier =
                    Modifier.padding(
                        horizontal = 3.dp,
                        vertical = 1.dp,
                    ),
            )
        }
    }
}

@Composable
private fun DestinationMarker() {
    Surface(
        shape =
            CircleShape,
        color =
            MaterialTheme
                .colorScheme
                .primary,
        shadowElevation =
            10.dp,
        modifier =
            Modifier.size(44.dp),
    ) {
        Box(
            contentAlignment =
                Alignment.Center,
        ) {
            Icon(
                imageVector =
                    Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.map_destination),
                tint =
                    MaterialTheme
                        .colorScheme
                        .onPrimary,
                modifier =
                    Modifier.size(26.dp),
            )
        }
    }
}

@Composable
private fun MapStatusChip(
    destinationStop: BusStop?,
    tripOptions: List<TripOption>,
    buses: Int,
    stops: Int,
    hasLocation: Boolean,
    modifier: Modifier = Modifier,
) {
    val text =
        when {
            destinationStop != null &&
                    tripOptions.isNotEmpty() -> {

                val count =
                    tripOptions.size

                stringResource(R.string.map_direct_routes_count, count)
            }

            destinationStop != null -> {
                stringResource(R.string.map_no_direct_routes)
            }

            hasLocation -> {
                stringResource(R.string.map_nearby_summary, stops, buses)
            }

            else -> {
                stringResource(R.string.map_buses_online, buses)
            }
        }

    Surface(
        modifier = modifier,
        shape =
            RoundedCornerShape(16.dp),
        color =
            MaterialTheme
                .colorScheme
                .surface,
        shadowElevation =
            4.dp,
    ) {
        Text(
            text = text,
            style =
                MaterialTheme
                    .typography
                    .bodyMedium,
            fontWeight =
                FontWeight.SemiBold,
            modifier =
                Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 10.dp,
                ),
        )
    }
}

@Composable
private fun TripOptionsCard(
    destinationStop: BusStop,
    options: List<TripOption>,
    isLoading: Boolean,

    onRouteClick: (
        routeId: String,
        selectedBusName: String?,
        selectedBusDirection: String?,
    ) -> Unit,

    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(24.dp),
        color =
            MaterialTheme
                .colorScheme
                .surface,
        shadowElevation =
            10.dp,
    ) {
        Column(
            modifier =
                Modifier.padding(18.dp),
        ) {

            Text(
                text =
                    destinationStop.name,
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                fontWeight =
                    FontWeight.Bold,
                maxLines = 1,
                overflow =
                    TextOverflow.Ellipsis,
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp),
            )

            Text(
                text =
                    stringResource(R.string.trip_direct_options),
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant,
            )

            when {

                isLoading -> {

                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                        contentAlignment =
                            Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(24.dp),
                            strokeWidth =
                                2.dp,
                        )
                    }
                }

                options.isEmpty() -> {

                    Text(
                        text =
                            stringResource(R.string.trip_no_direct_option),
                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant,
                        modifier =
                            Modifier.padding(
                                top = 16.dp,
                                bottom = 6.dp,
                            ),
                    )
                }

                else -> {

                    options
                        .take(3)
                        .forEach { option ->

                            TripOptionRow(
                                option =
                                    option,
                                onClick = {
                                    onRouteClick(
                                        option.route.id,
                                        null,
                                        null,
                                    )
                                },
                            )
                        }
                }
            }
        }
    }
}

@Composable
private fun TripOptionRow(
    option: TripOption,
    onClick: () -> Unit,
) {
    Surface(
        onClick =
            onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 10.dp,
                ),
        shape =
            RoundedCornerShape(16.dp),
        color =
            MaterialTheme
                .colorScheme
                .surfaceVariant
                .copy(
                    alpha = 0.45f
                ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            verticalAlignment =
                Alignment.CenterVertically,
        ) {

            Surface(
                shape =
                    RoundedCornerShape(12.dp),
                color =
                    MaterialTheme
                        .colorScheme
                        .primary,
                modifier =
                    Modifier.size(48.dp),
            ) {
                Box(
                    contentAlignment =
                        Alignment.Center,
                ) {
                    Text(
                        text =
                            option.route.number,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onPrimary,
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,
                        fontWeight =
                            FontWeight.Bold,
                    )
                }
            }

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(
                            start = 12.dp,
                        ),
            ) {

                Text(
                    text =
                        stringResource(R.string.trip_route_label, option.route.number),
                    style =
                        MaterialTheme
                            .typography
                            .bodyLarge,
                    fontWeight =
                        FontWeight.SemiBold,
                )

                Text(
                    text =
                        stringResource(
                            R.string.trip_walk_to_stop,
                            formatDistance(option.distanceToStartMeters),
                            walkingMinutes(option.distanceToStartMeters),
                        ),
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,
                    modifier =
                        Modifier.padding(
                            top = 2.dp,
                        ),
                )

                Text(
                    text =
                        option.startStop.name,
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis,
                )
            }

            Text(
                text = "›",
                style =
                    MaterialTheme
                        .typography
                        .headlineSmall,
                color =
                    MaterialTheme
                        .colorScheme
                        .primary,
            )
        }
    }
}

@Composable
private fun SelectedStopCard(
    nearbyStop: NearbyStop,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(24.dp),
        color =
            MaterialTheme
                .colorScheme
                .surface,
        shadowElevation =
            10.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
            verticalAlignment =
                Alignment.CenterVertically,
        ) {

            Surface(
                shape =
                    CircleShape,
                color =
                    MaterialTheme
                        .colorScheme
                        .primaryContainer,
                modifier =
                    Modifier.size(48.dp),
            ) {
                Box(
                    contentAlignment =
                        Alignment.Center,
                ) {
                    Icon(
                        imageVector =
                            Icons.Default.LocationOn,
                        contentDescription = null,
                        tint =
                            MaterialTheme
                                .colorScheme
                                .primary,
                    )
                }
            }

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(
                            start = 14.dp,
                        ),
            ) {

                Text(
                    text =
                        nearbyStop.stop.name,
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis,
                )

                Text(
                    text =
                        stringResource(
                            R.string.distance_from_you,
                            formatDistance(nearbyStop.distanceMeters),
                        ),
                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,
                    modifier =
                        Modifier.padding(
                            top = 4.dp,
                        ),
                )

                Text(
                    text = stringResource(
                        R.string.walking_time,
                        walkingMinutes(nearbyStop.distanceMeters),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 2.dp),
                )

                nearbyStop.stop.number?.let { number ->
                    Text(
                        text = stringResource(R.string.stop_number, number),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedBusCard(
    bus: Bus,
    routeNumber: String,
    onRouteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(24.dp),
        color =
            MaterialTheme
                .colorScheme
                .surface,
        shadowElevation =
            10.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
            verticalAlignment =
                Alignment.CenterVertically,
        ) {

            Surface(
                shape =
                    RoundedCornerShape(14.dp),
                color =
                    MaterialTheme
                        .colorScheme
                        .primary,
                modifier =
                    Modifier.size(52.dp),
            ) {
                Box(
                    contentAlignment =
                        Alignment.Center,
                ) {
                    Text(
                        text =
                            routeNumber,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onPrimary,
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,
                        fontWeight =
                            FontWeight.Bold,
                    )
                }
            }

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(
                            start = 14.dp,
                        ),
            ) {

                Text(
                    text = stringResource(
                        R.string.trip_route_label,
                        routeNumber,
                    ),
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                )

                Text(
                    text =
                        bus.name,
                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,
                    modifier =
                        Modifier.padding(
                            top = 3.dp,
                        ),
                )

                Text(
                    text = when (bus.direction) {
                        com.batumove.app.domain.model.Direction.OUTBOUND ->
                            stringResource(R.string.direction_outbound)
                        com.batumove.app.domain.model.Direction.INBOUND ->
                            stringResource(R.string.direction_inbound)
                        com.batumove.app.domain.model.Direction.UNKNOWN ->
                            stringResource(R.string.direction_unknown)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 2.dp),
                ) // selected_bus_direction
            }

            Button(
                onClick =
                    onRouteClick,
                shape =
                    RoundedCornerShape(14.dp),
            ) {
                Text(
                    text = stringResource(R.string.action_open),
                )
            }
        }
    }
}

@Composable
private fun NearbyStopsCard(
    stops: List<NearbyStop>,
    onStopClick: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 10.dp,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.nearby_stops_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.nearby_stops_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, bottom = 6.dp),
            )

            if (stops.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_nearby_stops),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            } else {
                stops.forEach { nearbyStop ->
                    Surface(
                        onClick = { onStopClick(nearbyStop.stop.id) },
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp),
                            )
                            Column(
                                modifier = Modifier.weight(1f).padding(start = 10.dp),
                            ) {
                                Text(
                                    text = nearbyStop.stop.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = stringResource(
                                        R.string.trip_walk_to_stop,
                                        formatDistance(nearbyStop.distanceMeters),
                                        walkingMinutes(nearbyStop.distanceMeters),
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Text(
                                text = "›",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun walkingMinutes(meters: Int): Int =
    (meters / 80.0).toInt().coerceAtLeast(1)

@Composable
private fun formatDistance(
    meters: Int,
): String =
    if (meters < 1_000) {
        stringResource(R.string.distance_meters, meters)
    } else {
        stringResource(
            R.string.distance_kilometers,
            String.format(Locale.getDefault(), "%.1f", meters / 1_000f),
        )
    }
