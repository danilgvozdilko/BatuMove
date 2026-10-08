package com.batumove.app.data.local.model

import androidx.room.Embedded
import com.batumove.app.data.local.entity.StopEntity

data class RouteStopWithStop(

    @Embedded
    val stop: StopEntity,

    val direction: Int,

    val order: Int,
)