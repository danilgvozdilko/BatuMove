package com.batumove.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.batumove.app.presentation.main.MainDestination


@Composable
fun BatuMoveBottomBar(
    selectedDestination: MainDestination,
    onDestinationSelected: (MainDestination) -> Unit,
) {
    NavigationBar(
        containerColor =
            MaterialTheme.colorScheme.surface,
    ) {

        MainDestination.entries.forEach { destination ->

            val selected =
                destination == selectedDestination

            NavigationBarItem(
                selected = selected,
                onClick = {
                    onDestinationSelected(
                        destination
                    )
                },
                icon = {

                    Icon(
                        imageVector =
                            destination.icon(
                                selected = selected
                            ),
                        contentDescription = null,
                    )
                },
                label = {

                    Text(
                        text = stringResource(
                            destination.titleRes
                        )
                    )
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor =
                            MaterialTheme
                                .colorScheme
                                .primary,

                        selectedTextColor =
                            MaterialTheme
                                .colorScheme
                                .primary,

                        indicatorColor =
                            MaterialTheme
                                .colorScheme
                                .primaryContainer
                                .copy(alpha = 0.35f),
                    ),
            )
        }
    }
}

private fun MainDestination.icon(
    selected: Boolean,
): ImageVector =
    when (this) {

        MainDestination.ROUTES ->
            if (selected) {
                Icons.Rounded.DirectionsBus
            } else {
                Icons.Outlined.DirectionsBus
            }

        MainDestination.MAP ->
            if (selected) {
                Icons.Rounded.Map
            } else {
                Icons.Outlined.Map
            }

        MainDestination.SETTINGS ->
            if (selected) {
                Icons.Rounded.Settings
            } else {
                Icons.Outlined.Settings
            }
    }