package com.batumove.app.data.mapper

import com.batumove.app.domain.model.Direction
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DirectionMapperTest {

    @Test
    fun `status 1 maps to outbound`() {
        val status = 1
        val result = status.toDirection()
        assertEquals(Direction.OUTBOUND, result)
    }

    @Test
    fun `status 2 maps to inbound`() {
        val status = 2
        val result = status.toDirection()
        assertEquals(Direction.INBOUND, result)
    }

    @Test
    fun `status -1 maps to unknown`() {
        val status = -1
        val result = status.toDirection()
        assertEquals(Direction.UNKNOWN, result)
    }

    @Test
    fun `unsupported status maps to unknown`() {
        val status = 999
        val result = status.toDirection()
        assertEquals(Direction.UNKNOWN, result)
    }
}