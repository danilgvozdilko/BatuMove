package com.batumove.app.domain.time

import com.batumove.app.domain.time.TimeProvider
import javax.inject.Inject

class SystemTimeProvider @Inject constructor() :
    TimeProvider {

    override fun currentTimeMillis(): Long =
        System.currentTimeMillis()
}