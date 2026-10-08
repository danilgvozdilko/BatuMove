package com.batumove.app.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.batumove.app.R
import com.batumove.app.ui.components.BatuMoveBrand
import com.batumove.app.ui.components.BatuMoveLoadingIndicator
import com.batumove.app.ui.components.BatumiLandscape
import com.batumove.app.ui.theme.BatuMoveDimens


@Composable
fun SplashRoute(
    onNavigateToMain: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        val state = uiState

        if (state is SplashUiState.Ready) {
            when (state.destination) {
                SplashDestination.Main -> {
                    onNavigateToMain()
                }

                SplashDestination.Language -> {
                    onNavigateToLanguage()
                }
            }
        }
    }

    SplashScreen(
        uiState = uiState,
        onRetry = viewModel::retry,
    )
}

@Composable
fun SplashScreen(
    uiState: SplashUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(
            modifier = Modifier.weight(0.65f)
        )

        BatuMoveBrand()

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        BatumiLandscape(
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        SplashStateContent(
            uiState = uiState,
            onRetry = onRetry,
        )

        Spacer(
            modifier = Modifier.weight(0.55f)
        )
    }
}

@Composable
private fun SplashStateContent(
    uiState: SplashUiState,
    onRetry: () -> Unit,
) {
    when (uiState) {

        SplashUiState.Loading -> {
            LoadingContent()
        }

        SplashUiState.Error -> {
            ErrorContent(
                onRetry = onRetry,
            )
        }

        is SplashUiState.Ready -> {
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        BatuMoveLoadingIndicator()

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = stringResource(
                R.string.splash_message
            ),
            color = MaterialTheme
                .colorScheme
                .onSurfaceVariant,
            style = MaterialTheme
                .typography
                .bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                horizontal = BatuMoveDimens
                    .screenHorizontalPadding
            ),
        )
    }
}

@Composable
private fun ErrorContent(
    onRetry: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(
            horizontal = BatuMoveDimens
                .screenHorizontalPadding
        ),
    ) {

        Text(
            text = stringResource(
                R.string.splash_error
            ),
            color = MaterialTheme
                .colorScheme
                .onSurfaceVariant,
            style = MaterialTheme
                .typography
                .bodyMedium,
            textAlign = TextAlign.Center,
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onRetry,
        ) {
            Text(
                text = stringResource(
                    R.string.action_retry
                )
            )
        }
    }
}