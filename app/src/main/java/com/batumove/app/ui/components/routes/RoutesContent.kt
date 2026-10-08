package com.batumove.app.ui.components.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.batumove.app.domain.model.Route

@Composable
fun RoutesContent(
    routes: List<Route>,
    onRouteClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            bottom = 24.dp,
        ),
    ) {
        items(
            items = routes,
            key = { route ->
                route.id
            },
        ) { route ->

            RouteCard(
                route = route,
                onClick = {
                    onRouteClick(route.id)
                },
            )
        }
    }
}