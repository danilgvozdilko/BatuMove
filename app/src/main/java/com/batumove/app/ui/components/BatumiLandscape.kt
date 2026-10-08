package com.batumove.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.batumove.app.R


@Composable
fun BatumiLandscape(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(
            id = R.drawable.splash_bg,
        ),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = modifier,
    )
}