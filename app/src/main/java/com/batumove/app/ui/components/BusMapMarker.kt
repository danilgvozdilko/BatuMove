package com.batumove.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BusMapMarker(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(
                if (selected) {
                    38.dp
                } else {
                    32.dp
                }
            )
            .clip(CircleShape)
            .background(
                MaterialTheme.colorScheme.primary
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
                MaterialTheme.colorScheme.onPrimary,
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