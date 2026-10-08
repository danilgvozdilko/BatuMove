package com.batumove.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MapBusMarker(
    routeNumber: String,
    selected: Boolean,
) {

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation =
            if (selected) {
                8.dp
            } else {
                3.dp
            },
    ) {

        Text(
            text = routeNumber,
            color =
                MaterialTheme.colorScheme.onPrimary,
            fontWeight =
                FontWeight.Bold,
            modifier =
                Modifier.padding(
                    horizontal = 9.dp,
                    vertical = 6.dp,
                ),
        )
    }
}