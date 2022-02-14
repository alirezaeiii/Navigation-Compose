package com.android.sample.app.database.section

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: DbSection)

    @Query("SELECT * FROM sections WHERE id=:id")
    suspend fun getSection(id: String): DbSection?
}