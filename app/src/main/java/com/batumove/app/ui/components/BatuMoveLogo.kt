package com.batumove.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.batumove.app.R

@Composable
fun BatuMoveLogo(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(
            id = R.drawable.ic_bus_logo,
        ),
        contentDescription = null,
        modifier = modifier,
    )
}