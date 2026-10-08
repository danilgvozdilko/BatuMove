package com.batumove.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.batumove.app.data.local.entity.RouteStopEntity
import com.batumove.app.data.local.entity.StopEntity

@Dao
interface StopDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertAll(
        stops: List<StopEntity>,
    )

    @Query(
        """
        SELECT COUNT(*)
        FROM stops
        """
    )
    suspend fun count(): Int

    @Query(
        """
    SELECT *
    FROM stops
    ORDER BY name
    """
    )
    suspend fun getStops(): List<StopEntity>

    @Query(
        """
        DELETE FROM stops
        """
    )
    suspend fun clear()
}