package com.batumove.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import com.batumove.app.ui.theme.GeorgianRedLight

@Composable
fun BatuMoveLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition =
        rememberInfiniteTransition(
            label = "BatuMoveLoading"
        )

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 850,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "LoadingProgress",
    )

    val firstWidth = lerp(
        start = 56.dp,
        stop = 30.dp,
        fraction = progress,
    )

    val secondWidth = lerp(
        start = 26.dp,
        stop = 52.dp,
        fraction = progress,
    )

    val firstAlpha = lerp(
        start = 1f,
        stop = 0.45f,
        fraction = progress,
    )

    val secondAlpha = lerp(
        start = 0.45f,
        stop = 1f,
        fraction = progress,
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier = Modifier
                .width(firstWidth)
                .height(8.dp)
                .clip(
                    RoundedCornerShape(100.dp)
                )
                .background(
                    MaterialTheme.colorScheme.primary
                        .copy(alpha = firstAlpha)
                ),
        )

        Box(
            modifier = Modifier
                .width(secondWidth)
                .height(8.dp)
                .clip(
                    RoundedCornerShape(100.dp)
                )
                .background(
                    GeorgianRedLight.copy(
                        alpha = secondAlpha
                    )
                ),
        )
    }
}