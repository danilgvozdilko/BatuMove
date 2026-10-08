package com.batumove.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.batumove.app.data.local.entity.RouteGeometryPointEntity

@Dao
interface RouteGeometryDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertAll(
        points: List<RouteGeometryPointEntity>,
    )

    @Query(
        """
        SELECT *
        FROM route_geometry
        WHERE routeId = :routeId
        ORDER BY pointOrder
        """
    )
    suspend fun getRouteGeometry(
        routeId: String,
    ): List<RouteGeometryPointEntity>

    @Query(
        """
        DELETE FROM route_geometry
        """
    )
    suspend fun clear()
}