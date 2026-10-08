package com.batumove.app.ui.components.routes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.batumove.app.domain.model.Route

@Composable
fun RouteCard(
    route: Route,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        shape =
            RoundedCornerShape(20.dp),
        color =
            MaterialTheme.colorScheme.surface,
        border =
            BorderStroke(
                width = 1.dp,
                color =
                    MaterialTheme
                        .colorScheme
                        .outlineVariant,
            ),
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 16.dp,
            ),
            verticalAlignment =
                Alignment.CenterVertically,
        ) {

            RouteNumberBadge(
                number = route.number,
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Text(
                text = stringResource(
                    R.string.route_number,
                    route.number,
                ),
                style =
                    MaterialTheme.typography.titleLarge,
                color =
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )

            Text(
                text = "›",
                style =
                    MaterialTheme.typography.headlineMedium,
                color =
                    MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun RouteNumberBadge(
    number: String,
) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(
                MaterialTheme.colorScheme.primary
            ),
        contentAlignment =
            Alignment.Center,
    ) {

        Text(
            text = number,
            style =
                MaterialTheme.typography.titleLarge,
            color =
                MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
        )
    }
}
