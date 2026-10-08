package com.batumove.app.data.mapper

import com.batumove.app.data.remote.dto.BusDto
import com.batumove.app.domain.model.Direction
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BusMapperTest {

    @Test
    fun `bus dto maps to domain bus`() {
        val dto = BusDto(44.512, 44.6125, status = 1, "CN 406 NC")

        val routeId = "5ed2bd4f657784b5a98a8c7e"

        // Act
        val result = dto.toDomain(routeId)

        assertEquals("CN 406 NC", result.name)
        assertEquals(routeId, result.routeId)
        assertEquals(44.512, result.position.latitude)
        assertEquals(44.6125, result.position.longitude)
        assertEquals(Direction.OUTBOUND, result.direction)

    }
}