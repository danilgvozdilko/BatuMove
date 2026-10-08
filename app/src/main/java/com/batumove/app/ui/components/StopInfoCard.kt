package com.batumove.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.batumove.app.R
import com.batumove.app.domain.model.NextBusArrival
import com.batumove.app.domain.model.RouteStop

@Composable
fun StopInfoCard(
    routeStop: RouteStop,
    nextBusArrival: NextBusArrival?,
    modifier: Modifier = Modifier,
) {
    val stop =
        routeStop.stop

    Surface(
        modifier =
            modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(16.dp),
        color =
            MaterialTheme.colorScheme.surface,
        shadowElevation =
            6.dp,
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp),
        ) {

            Text(
                text =
                    stop.name,
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                fontWeight =
                    FontWeight.SemiBold,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface,
            )

            stop.number?.let { number ->

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text =
                        stringResource(
                            R.string.stop_number,
                            number,
                        ),
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,
                )
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            if (
                nextBusArrival == null
            ) {

                Text(
                    text =
                        stringResource(
                            R.string
                                .no_approaching_buses
                        ),
                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,
                )

            } else {

                Text(
                    text =
                        stringResource(
                            R.string.next_bus
                        ),
                    style =
                        MaterialTheme
                            .typography
                            .labelLarge,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                NextBusRow(
                    arrival =
                        nextBusArrival,
                )
            }
        }
    }
}

@Composable
private fun NextBusRow(
    arrival: NextBusArrival,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme
                        .colorScheme
                        .primary
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector =
                    Icons.Rounded.DirectionsBus,
                contentDescription = null,
                tint =
                    MaterialTheme
                        .colorScheme
                        .onPrimary,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
        ) {

            Text(
                text = arrival.bus.name,
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                fontWeight = FontWeight.SemiBold,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface,
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = stringResource(
                    R.string.bus_arrival_details,
                    formatDistance(
                        arrival.distanceMeters
                    ),
                    arrival.stopsAway,
                ),
                style =
                    MaterialTheme
                        .typography
                        .bodySmall,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant,
            )
        }

        arrival.etaMinutes?.let { minutes ->

            Text(
                text = stringResource(
                    R.string.estimated_minutes,
                    minutes,
                ),
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                color =
                    MaterialTheme
                        .colorScheme
                        .primary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun formatDistance(
    distanceMeters: Int,
): String =
    if (distanceMeters < 1000) {
        stringResource(R.string.distance_meters, distanceMeters)
    } else {
        stringResource(
            R.string.distance_kilometers,
            String.format("%.1f", distanceMeters / 1000.0),
        )
    }
