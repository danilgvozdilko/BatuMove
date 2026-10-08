package com.batumove.app.ui.components.routeDetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DirectionButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clickable(
                onClick = onClick
            ),
        shape =
            RoundedCornerShape(12.dp),
        color =
            if (selected) {
                MaterialTheme
                    .colorScheme
                    .primary
            } else {
                Color.Transparent
            },
    ) {

        Text(
            text = text,
            textAlign = TextAlign.Center,
            color =
                if (selected) {
                    MaterialTheme
                        .colorScheme
                        .onPrimary
                } else {
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
                },
            modifier =
                Modifier.padding(
                    vertical = 10.dp
                ),
        )
    }
}