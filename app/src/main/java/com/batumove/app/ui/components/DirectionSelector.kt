package com.batumove.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.batumove.app.R
import com.batumove.app.domain.model.Direction
import com.batumove.app.ui.components.routeDetails.DirectionButton

@Composable
fun DirectionSelector(
    selectedDirection: Direction,
    onDirectionSelected: (Direction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(
                MaterialTheme
                    .colorScheme
                    .surfaceVariant
            )
            .padding(4.dp),
    ) {

        DirectionButton(
            text = stringResource(
                R.string.direction_outbound
            ),
            selected =
                selectedDirection ==
                        Direction.OUTBOUND,
            onClick = {
                onDirectionSelected(
                    Direction.OUTBOUND
                )
            },
            modifier =
                Modifier.weight(1f),
        )

        DirectionButton(
            text = stringResource(
                R.string.direction_inbound
            ),
            selected =
                selectedDirection ==
                        Direction.INBOUND,
            onClick = {
                onDirectionSelected(
                    Direction.INBOUND
                )
            },
            modifier =
                Modifier.weight(1f),
        )
    }
}
