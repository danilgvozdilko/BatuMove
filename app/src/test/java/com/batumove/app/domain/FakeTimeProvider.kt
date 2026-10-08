package com.batumove.app.domain

import com.batumove.app.domain.time.TimeProvider

class FakeTimeProvider(
    var currentTime: Long = 0L,
) : TimeProvider {

    override fun currentTimeMillis(): Long =
        currentTime
}