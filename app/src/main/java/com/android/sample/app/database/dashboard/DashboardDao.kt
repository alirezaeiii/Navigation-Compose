package com.android.sample.app.database.dashboard

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DashboardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dashboard: DashboardEntity)

    @Query("SELECT * FROM dashboard")
    suspend fun getDashboard(): DashboardEntity?
}