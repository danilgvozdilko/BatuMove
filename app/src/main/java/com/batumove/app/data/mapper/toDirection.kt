package com.batumove.app.data.mapper

import com.batumove.app.domain.model.Direction

internal fun Int.toDirection(): Direction =
    when (this) {
        1 -> Direction.OUTBOUND
        2 -> Direction.INBOUND
        else -> Direction.UNKNOWN
    }