package com.batumove.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.batumove.app.data.local.entity.RouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {

    @Query(
        """
    SELECT *
    FROM routes
    WHERE id = :routeId
    LIMIT 1
    """
    )
    suspend fun getRoute(
        routeId: String,
    ): RouteEntity?

    @Query(
        """
        SELECT *
        FROM routes
        ORDER BY sortOrder
        """
    )
    fun observeRoutes(): Flow<List<RouteEntity>>

    @Query(
        """
        SELECT *
        FROM routes
        ORDER BY sortOrder
        """
    )
    suspend fun getRoutes(): List<RouteEntity>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertAll(
        routes: List<RouteEntity>,
    )

    @Query(
        """
        SELECT COUNT(*)
        FROM routes
        """
    )
    suspend fun count(): Int

    @Query(
        """
        DELETE FROM routes
        """
    )
    suspend fun clear()
}