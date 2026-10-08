package com.batumove.app.presentation.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.batumove.app.R
import com.batumove.app.ui.components.routes.BatuHeader
import com.batumove.app.ui.components.routes.RoutesContent
import com.batumove.app.ui.components.routes.RoutesEmpty
import com.batumove.app.ui.components.routes.RoutesError
import com.batumove.app.ui.components.routes.RoutesLoading
import com.batumove.app.ui.theme.BatuMoveDimens


@Composable
fun RoutesRoute(
    onRouteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoutesViewModel = hiltViewModel(),
) {
    val uiState by
    viewModel.uiState
        .collectAsStateWithLifecycle()

    RoutesScreen(
        uiState = uiState,
        onRouteClick = onRouteClick,
        onRetry = viewModel::retry,
        modifier = modifier,
    )
}

@Composable
fun RoutesScreen(
    uiState: RoutesUiState,
    onRouteClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(
                horizontal = BatuMoveDimens.screenHorizontalPadding
            ),
    ) {

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        BatuHeader(stringResource(R.string.routes_title))

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            when (uiState) {

                RoutesUiState.Loading -> {
                    RoutesLoading()
                }

                RoutesUiState.Empty -> {
                    RoutesEmpty()
                }

                RoutesUiState.Error -> {
                    RoutesError(
                        onRetry = onRetry
                    )
                }

                is RoutesUiState.Content -> {
                    RoutesContent(
                        routes = uiState.routes,
                        onRouteClick = onRouteClick,
                    )
                }
            }
        }
    }
}