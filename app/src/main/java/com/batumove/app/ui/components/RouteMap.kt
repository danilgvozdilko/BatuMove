package com.batumove.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.batumove.app.domain.model.Bus
import com.batumove.app.domain.model.GeoPoint
import com.batumove.app.domain.model.NextBusArrival
import com.batumove.app.domain.model.RouteGeometry
import com.batumove.app.domain.model.RouteStop
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


private const val BUS_ANIMATION_DURATION_MS = 1_500

private val Bus.stableKey: String
    get() = "$routeId:$name"

@Composable
fun RouteMap(
    geometry: RouteGeometry?,
    stops: List<RouteStop>,
    buses: List<Bus>,
    nextBusArrival: NextBusArrival?,
    selectedStopId: String?,
    highlightedStopId: String?,
    initiallySelectedBusName: String?,
    userLocation: GeoPoint?,
    locationPermissionGranted: Boolean,
    onStopSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedBusKey by remember {
        mutableStateOf<String?>(null)
    }

    val selectedBus =
        remember(
            buses,
            selectedBusKey,
        ) {
            buses.firstOrNull { bus ->
                bus.stableKey ==
                        selectedBusKey
            }
        }

    val selectedStop =
        remember(
            stops,
            selectedStopId,
        ) {
            stops.firstOrNull {
                it.stop.id ==
                        selectedStopId
            }
        }

    val highlightedStop =
        remember(
            stops,
            highlightedStopId,
        ) {
            stops.firstOrNull {
                it.stop.id ==
                        highlightedStopId
            }
        }

    LaunchedEffect(
        initiallySelectedBusName,
        buses,
    ) {
        val busName =
            initiallySelectedBusName
                ?: return@LaunchedEffect

        val bus =
            buses.firstOrNull {
                it.name == busName
            }
                ?: return@LaunchedEffect

        selectedBusKey =
            bus.stableKey
    }

    LaunchedEffect(
        buses,
        selectedBusKey,
    ) {
        if (
            selectedBusKey != null &&
            selectedBus == null
        ) {
            selectedBusKey = null
        }
    }

    val batumi = remember {
        LatLng(
            41.6461,
            41.6405,
        )
    }

    val cameraPositionState =
        rememberCameraPositionState {
            position =
                CameraPosition.fromLatLngZoom(
                    batumi,
                    13f,
                )
        }

    val uiSettings =
        remember {
            MapUiSettings(
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true,
                scrollGesturesEnabled = true,
                rotationGesturesEnabled = true,
                tiltGesturesEnabled = true,
                mapToolbarEnabled = false,
                compassEnabled = true,
            )
        }

    val mapProperties =
        remember(
            locationPermissionGranted
        ) {
            MapProperties(
                isMyLocationEnabled =
                    locationPermissionGranted,
            )
        }

    val routePoints =
        remember(geometry) {
            geometry
                ?.points
                ?.map { point ->
                    LatLng(
                        point.latitude,
                        point.longitude,
                    )
                }
                .orEmpty()
        }

    LaunchedEffect(
        routePoints,
        userLocation,
    ) {
        if (userLocation != null) {
            return@LaunchedEffect
        }

        if (routePoints.size < 2) {
            return@LaunchedEffect
        }

        val boundsBuilder =
            LatLngBounds.builder()

        routePoints.forEach { point ->
            boundsBuilder.include(point)
        }

        cameraPositionState.animate(
            update =
                CameraUpdateFactory
                    .newLatLngBounds(
                        boundsBuilder.build(),
                        80,
                    ),

            durationMs = 700,
        )
    }

    LaunchedEffect(
        selectedStopId
    ) {
        if (
            selectedStopId == null ||
            selectedStopId ==
            highlightedStopId
        ) {
            return@LaunchedEffect
        }

        val stop =
            selectedStop?.stop
                ?: return@LaunchedEffect

        cameraPositionState.animate(
            update =
                CameraUpdateFactory
                    .newLatLngZoom(
                        LatLng(
                            stop.position.latitude,
                            stop.position.longitude,
                        ),
                        16f,
                    ),

            durationMs =
                600,
        )
    }

    Box(
        modifier = modifier,
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState =
                cameraPositionState,
            properties = mapProperties,
            uiSettings =
                uiSettings,
            onMapClick = {
                selectedBusKey = null
                onStopSelected(null)
            },
        ) {
            if (routePoints.isNotEmpty()) {
                Polyline(
                    points = routePoints,
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary,
                    width = 10f,
                    geodesic = true,
                    zIndex = 1f,
                )
            }

            stops.forEachIndexed { index, routeStop ->

                val stop =
                    routeStop.stop

                val isTerminal =
                    index == 0 ||
                            index == stops.lastIndex

                val isSelected =
                    selectedStopId ==
                            stop.id

                val isHighlighted =
                    highlightedStopId ==
                            stop.id

                MarkerComposable(
                    state =
                        rememberUpdatedMarkerState(
                            position =
                                LatLng(
                                    stop.position.latitude,
                                    stop.position.longitude,
                                )
                        ),

                    title =
                        stop.name,

                    zIndex =
                        when {
                            isHighlighted -> 15f
                            isSelected -> 10f
                            else -> 5f
                        },

                    onClick = {
                        selectedBusKey = null
                        onStopSelected(stop.id)
                        true
                    },
                ) {

                    RouteStopMarker(
                        isTerminal =
                            isTerminal,

                        selected =
                            isSelected,

                        highlighted =
                            isHighlighted,
                    )
                }
            }

            buses.forEach { bus ->

                key(bus.stableKey) {

                    val bearing =
                        remember(
                            bus.position,
                            bus.direction,
                            geometry,
                        ) {
                            calculateRouteBearing(
                                bus = bus,
                                geometry = geometry,
                            )
                        }

                    AnimatedBusMarker(
                        bus = bus,
                        bearing = bearing,
                        selected =
                            selectedBusKey ==
                                    bus.stableKey,
                        onClick = {
                            onStopSelected(null)
                            selectedBusKey =
                                bus.stableKey
                        },
                    )
                }
            }
        }

        when {

            selectedBus != null -> {

                BusInfoCard(
                    bus = selectedBus,
                    modifier = Modifier
                        .align(
                            Alignment.BottomCenter
                        )
                        .padding(12.dp),
                )
            }

            selectedStop != null -> {

                StopInfoCard(
                    routeStop = selectedStop,
                    nextBusArrival = nextBusArrival,
                    modifier = Modifier
                        .align(
                            Alignment.BottomCenter
                        )
                        .padding(12.dp),
                )
            }
        }
    }
}

@Composable
private fun StopMapMarker(
    isTerminal: Boolean,
    selected: Boolean,
) {
    val markerSize =
        when {
            selected -> 22.dp
            isTerminal -> 18.dp
            else -> 14.dp
        }

    Box(
        modifier = Modifier
            .size(markerSize)
            .background(
                color =
                    if (
                        selected ||
                        isTerminal
                    ) {
                        MaterialTheme
                            .colorScheme
                            .primary
                    } else {
                        Color.White
                    },
                shape = CircleShape,
            )
            .border(
                width =
                    if (selected) {
                        4.dp
                    } else {
                        3.dp
                    },
                color =
                    if (selected) {
                        Color.White
                    } else {
                        MaterialTheme
                            .colorScheme
                            .primary
                    },
                shape = CircleShape,
            ),
    )
}

@Composable
private fun AnimatedBusMarker(
    bus: Bus,
    bearing: Float,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val targetLatitude =
        bus.position.latitude

    val targetLongitude =
        bus.position.longitude

    val latitude =
        remember {
            Animatable(
                initialValue =
                    targetLatitude.toFloat()
            )
        }

    val longitude =
        remember {
            Animatable(
                initialValue =
                    targetLongitude.toFloat()
            )
        }

    LaunchedEffect(
        targetLatitude,
        targetLongitude,
    ) {
        coroutineScope {

            launch {
                latitude.animateTo(
                    targetValue =
                        targetLatitude.toFloat(),
                    animationSpec =
                        tween(
                            durationMillis =
                                BUS_ANIMATION_DURATION_MS,
                        ),
                )
            }

            launch {
                longitude.animateTo(
                    targetValue =
                        targetLongitude.toFloat(),
                    animationSpec =
                        tween(
                            durationMillis =
                                BUS_ANIMATION_DURATION_MS,
                        ),
                )
            }
        }
    }

    val markerState =
        rememberUpdatedMarkerState(
            position =
                LatLng(
                    latitude.value.toDouble(),
                    longitude.value.toDouble(),
                )
        )

    MarkerComposable(
        state = markerState,
        zIndex =
            if (selected) {
                20f
            } else {
                10f
            },
        onClick = {
            onClick()
            true
        },
    ) {
        BusMapMarker(
            selected = selected,
            bearing = bearing,
        )
    }
}

@Composable
private fun BusMapMarker(
    selected: Boolean,
    bearing: Float,
) {
    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector =
                Icons.Rounded.Navigation,
            contentDescription = null,
            tint =
                MaterialTheme
                    .colorScheme
                    .primary,
            modifier = Modifier
                .size(18.dp)
                .graphicsLayer {
                    rotationZ = bearing
                },
        )

        Box(
            modifier = Modifier
                .size(
                    if (selected) {
                        38.dp
                    } else {
                        32.dp
                    }
                )
                .clip(CircleShape)
                .background(
                    MaterialTheme
                        .colorScheme
                        .primary
                )
                .border(
                    width =
                        if (selected) {
                            3.dp
                        } else {
                            2.dp
                        },
                    color = Color.White,
                    shape = CircleShape,
                ),
            contentAlignment =
                Alignment.Center,
        ) {
            Icon(
                imageVector =
                    Icons.Rounded.DirectionsBus,
                contentDescription = null,
                tint =
                    MaterialTheme
                        .colorScheme
                        .onPrimary,
                modifier = Modifier.size(
                    if (selected) {
                        21.dp
                    } else {
                        18.dp
                    }
                ),
            )
        }
    }
}

private fun calculateRouteBearing(
    bus: Bus,
    geometry: RouteGeometry?,
): Float {
    val points =
        geometry?.points
            ?: return 0f

    if (points.size < 2) {
        return 0f
    }

    val nearestSegmentIndex =
        (0 until points.lastIndex)
            .minByOrNull { index ->

                val start =
                    points[index]

                val end =
                    points[index + 1]

                distanceToSegmentSquared(
                    pointLatitude =
                        bus.position.latitude,
                    pointLongitude =
                        bus.position.longitude,
                    startLatitude =
                        start.latitude,
                    startLongitude =
                        start.longitude,
                    endLatitude =
                        end.latitude,
                    endLongitude =
                        end.longitude,
                )
            }
            ?: return 0f

    val start =
        points[nearestSegmentIndex]

    val end =
        points[nearestSegmentIndex + 1]

    return bearingBetween(
        fromLatitude =
            start.latitude,
        fromLongitude =
            start.longitude,
        toLatitude =
            end.latitude,
        toLongitude =
            end.longitude,
    )
}

private fun distanceToSegmentSquared(
    pointLatitude: Double,
    pointLongitude: Double,
    startLatitude: Double,
    startLongitude: Double,
    endLatitude: Double,
    endLongitude: Double,
): Double {
    val segmentLatitude =
        endLatitude -
                startLatitude

    val segmentLongitude =
        endLongitude -
                startLongitude

    val pointToStartLatitude =
        pointLatitude -
                startLatitude

    val pointToStartLongitude =
        pointLongitude -
                startLongitude

    val segmentLengthSquared =
        segmentLatitude *
                segmentLatitude +
                segmentLongitude *
                segmentLongitude

    if (segmentLengthSquared == 0.0) {
        return (
                pointToStartLatitude *
                        pointToStartLatitude
                ) +
                (
                        pointToStartLongitude *
                                pointToStartLongitude
                        )
    }

    val projection =
        (
                pointToStartLatitude *
                        segmentLatitude +
                        pointToStartLongitude *
                        segmentLongitude
                ) /
                segmentLengthSquared

    val clampedProjection =
        projection.coerceIn(
            minimumValue = 0.0,
            maximumValue = 1.0,
        )

    val nearestLatitude =
        startLatitude +
                clampedProjection *
                segmentLatitude

    val nearestLongitude =
        startLongitude +
                clampedProjection *
                segmentLongitude

    val latitudeDifference =
        pointLatitude -
                nearestLatitude

    val longitudeDifference =
        pointLongitude -
                nearestLongitude

    return (
            latitudeDifference *
                    latitudeDifference
            ) +
            (
                    longitudeDifference *
                            longitudeDifference
                    )
}

private fun bearingBetween(
    fromLatitude: Double,
    fromLongitude: Double,
    toLatitude: Double,
    toLongitude: Double,
): Float {
    val fromLat =
        Math.toRadians(
            fromLatitude
        )

    val toLat =
        Math.toRadians(
            toLatitude
        )

    val longitudeDifference =
        Math.toRadians(
            toLongitude -
                    fromLongitude
        )

    val y =
        sin(
            longitudeDifference
        ) *
                cos(toLat)

    val x =
        cos(fromLat) *
                sin(toLat) -
                sin(fromLat) *
                cos(toLat) *
                cos(
                    longitudeDifference
                )

    val bearing =
        Math.toDegrees(
            atan2(
                y,
                x,
            )
        )

    return normalizeBearing(
        bearing.toFloat()
    )
}

private fun normalizeBearing(
    bearing: Float,
): Float =
    (bearing + 360f) % 360f

@Composable
private fun RouteStopMarker(
    isTerminal: Boolean,
    selected: Boolean,
    highlighted: Boolean,
) {
    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally,
    ) {

        if (highlighted) {

            Surface(
                shape =
                    RoundedCornerShape(8.dp),

                color =
                    MaterialTheme
                        .colorScheme
                        .primary,

                shadowElevation =
                    4.dp,
            ) {

                Text(
                    text =
                        "Ваша остановка",

                    color =
                        MaterialTheme
                            .colorScheme
                            .onPrimary,

                    fontSize =
                        9.sp,

                    lineHeight =
                        10.sp,

                    fontWeight =
                        FontWeight.Bold,

                    modifier =
                        Modifier.padding(
                            horizontal = 6.dp,
                            vertical = 3.dp,
                        ),
                )
            }

            Spacer(
                modifier =
                    Modifier.size(3.dp)
            )
        }

        val markerSize =
            when {
                highlighted -> 28.dp
                selected -> 22.dp
                isTerminal -> 18.dp
                else -> 14.dp
            }

        Box(
            modifier = Modifier
                .size(markerSize)
                .background(
                    color =
                        if (
                            highlighted ||
                            selected ||
                            isTerminal
                        ) {
                            MaterialTheme
                                .colorScheme
                                .primary
                        } else {
                            Color.White
                        },

                    shape =
                        CircleShape,
                )
                .border(
                    width =
                        when {
                            highlighted -> 4.dp
                            selected -> 4.dp
                            else -> 3.dp
                        },

                    color =
                        if (
                            highlighted ||
                            selected
                        ) {
                            Color.White
                        } else {
                            MaterialTheme
                                .colorScheme
                                .primary
                        },

                    shape =
                        CircleShape,
                ),
        )
    }
}