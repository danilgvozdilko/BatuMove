package com.batumove.app.ui.components.routeDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.batumove.app.R
import com.batumove.app.domain.model.Direction
import com.batumove.app.domain.model.RouteStop
import com.batumove.app.presentation.route_details.RouteDetailsUiState
import com.batumove.app.ui.components.DirectionSelector
import com.batumove.app.ui.components.RouteMap
import com.batumove.app.ui.theme.BatuMoveDimens

@Composable
fun RouteDetailsContent(
    uiState: RouteDetailsUiState,
    onDirectionSelected: (Direction) -> Unit,
    onStopSelected: (String?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal =
                    BatuMoveDimens
                        .screenHorizontalPadding,
            ),
    ) {

        Text(
            text =
                stringResource(
                    R.string.route_number,
                    uiState
                        .route
                        ?.number
                        .orEmpty(),
                ),

            style =
                MaterialTheme
                    .typography
                    .headlineLarge,

            color =
                MaterialTheme
                    .colorScheme
                    .onBackground,
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        DirectionSelector(
            selectedDirection =
                uiState.direction,

            onDirectionSelected =
                onDirectionSelected,
        )

        uiState.stops.lastOrNull()?.let { terminal ->
            Text(
                text = stringResource(
                    R.string.direction_towards,
                    terminal.stop.name,
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        RouteMap(
            geometry =
                uiState.geometry,

            stops =
                uiState.stops,

            buses =
                uiState.directionBuses,

            selectedStopId =
                uiState.selectedStopId,

            highlightedStopId =
                uiState.highlightedStopId,

            initiallySelectedBusName =
                uiState.selectedBusName,

            userLocation =
                uiState.userLocation,

            locationPermissionGranted =
                uiState
                    .locationPermissionGranted,

            nextBusArrival =
                uiState.nextBusArrival,

            onStopSelected =
                onStopSelected,

            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(
                    RoundedCornerShape(20.dp)
                ),
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        BusesStatus(
            busesCount =
                uiState
                    .directionBuses
                    .size,

            hasError =
                uiState.busesError,
        )

        Spacer(
            modifier =
                Modifier.height(28.dp)
        )

        Text(
            text =
                stringResource(
                    R.string.route_stops_title
                ),

            style =
                MaterialTheme
                    .typography
                    .titleLarge,

            color =
                MaterialTheme
                    .colorScheme
                    .onBackground,
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        StopsList(
            stops =
                uiState.stops,

            selectedStopId =
                uiState.selectedStopId,

            highlightedStopId =
                uiState.highlightedStopId,

            onStopClick = { stopId ->
                onStopSelected(stopId)
            },
        )
    }
}

@Composable
private fun BusesStatus(
    busesCount: Int,
    hasError: Boolean,
) {
    if (hasError) {
        Text(
            text = stringResource(R.string.buses_realtime_unavailable),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (busesCount == 1) {
                stringResource(R.string.bus_online_single)
            } else {
                stringResource(R.string.buses_online, busesCount)
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = stringResource(R.string.live_data),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun StopsList(
    stops: List<RouteStop>,
    selectedStopId: String?,
    highlightedStopId: String?,
    onStopClick: (String) -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier.fillMaxWidth(),

        verticalArrangement =
            Arrangement.spacedBy(0.dp),

        contentPadding =
            PaddingValues(
                bottom = 32.dp
            ),
    ) {

        itemsIndexed(
            items = stops,

            key = { _, routeStop ->
                routeStop.stop.id
            },
        ) { index, routeStop ->

            val stopId =
                routeStop.stop.id

            val selected =
                stopId ==
                        selectedStopId

            val highlighted =
                stopId ==
                        highlightedStopId

            StopItem(
                routeStop =
                    routeStop,

                first =
                    index == 0,

                last =
                    index ==
                            stops.lastIndex,

                selected =
                    selected,

                highlighted =
                    highlighted,

                onClick = {
                    onStopClick(stopId)
                },
            )
        }
    }
}

@Composable
private fun StopItem(
    routeStop: RouteStop,
    first: Boolean,
    last: Boolean,
    selected: Boolean,
    highlighted: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()

            .clip(
                RoundedCornerShape(12.dp)
            )

            .clickable(
                onClick = onClick
            )

            .background(
                when {

                    highlighted -> {
                        MaterialTheme
                            .colorScheme
                            .primaryContainer
                            .copy(
                                alpha = 0.45f
                            )
                    }

                    selected -> {
                        MaterialTheme
                            .colorScheme
                            .primaryContainer
                            .copy(
                                alpha = 0.25f
                            )
                    }

                    else -> {
                        Color.Transparent
                    }
                }
            )

            .padding(
                horizontal = 8.dp,
            ),

        verticalAlignment =
            Alignment.CenterVertically,
    ) {

        StopTimeline(
            first = first,
            last = last,
            selected = selected,
            highlighted = highlighted,
        )

        Spacer(
            modifier =
                Modifier.width(16.dp)
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(
                        vertical = 12.dp
                    ),
        ) {

            if (highlighted) {

                Text(
                    text = "Ваша остановка",

                    style =
                        MaterialTheme
                            .typography
                            .labelMedium,

                    color =
                        MaterialTheme
                            .colorScheme
                            .primary,

                    fontWeight =
                        FontWeight.Bold,
                )

                Spacer(
                    modifier =
                        Modifier.height(2.dp)
                )
            }

            Text(
                text =
                    routeStop.stop.name,

                style =
                    MaterialTheme
                        .typography
                        .bodyLarge,

                color =
                    if (
                        selected ||
                        highlighted
                    ) {
                        MaterialTheme
                            .colorScheme
                            .primary
                    } else {
                        MaterialTheme
                            .colorScheme
                            .onSurface
                    },

                fontWeight =
                    if (
                        selected ||
                        highlighted
                    ) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    },
            )
        }
    }
}

@Composable
private fun StopTimeline(
    first: Boolean,
    last: Boolean,
    selected: Boolean,
    highlighted: Boolean,
) {
    Column(
        modifier =
            Modifier.width(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,
    ) {

        Box(
            modifier = Modifier
                .width(2.dp)
                .height(14.dp)
                .background(
                    if (first) {
                        Color.Transparent
                    } else {
                        MaterialTheme
                            .colorScheme
                            .primary
                    }
                )
        )

        Box(
            modifier = Modifier
                .size(
                    when {
                        highlighted -> 18.dp
                        selected -> 16.dp
                        else -> 12.dp
                    }
                )
                .clip(
                    CircleShape
                )
                .background(
                    MaterialTheme
                        .colorScheme
                        .primary
                )
                .then(
                    if (
                        highlighted ||
                        selected
                    ) {
                        Modifier.border(
                            width =
                                if (highlighted) {
                                    4.dp
                                } else {
                                    3.dp
                                },

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .primaryContainer,

                            shape =
                                CircleShape,
                        )
                    } else {
                        Modifier
                    }
                )
        )

        Box(
            modifier = Modifier
                .width(2.dp)
                .height(30.dp)
                .background(
                    if (last) {
                        Color.Transparent
                    } else {
                        MaterialTheme
                            .colorScheme
                            .primary
                    }
                )
        )
    }
}