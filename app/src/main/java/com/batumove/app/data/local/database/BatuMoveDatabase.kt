package com.batumove.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.batumove.app.data.local.dao.RouteDao
import com.batumove.app.data.local.dao.RouteGeometryDao
import com.batumove.app.data.local.dao.RouteStopDao
import com.batumove.app.data.local.dao.StopDao
import com.batumove.app.data.local.dao.TransportTransactionDao
import com.batumove.app.data.local.entity.RouteEntity
import com.batumove.app.data.local.entity.RouteGeometryPointEntity
import com.batumove.app.data.local.entity.RouteStopEntity
import com.batumove.app.data.local.entity.StopEntity

@Database(
    entities = [
        RouteEntity::class,
        StopEntity::class,
        RouteStopEntity::class,
        RouteGeometryPointEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class BatuMoveDatabase :
    RoomDatabase() {

    abstract fun routeDao(): RouteDao

    abstract fun stopDao(): StopDao

    abstract fun routeStopDao(): RouteStopDao

    abstract fun routeGeometryDao():
            RouteGeometryDao

    abstract fun transportTransactionDao():
            TransportTransactionDao
}