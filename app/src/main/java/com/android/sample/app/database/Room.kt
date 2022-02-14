package com.android.sample.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.sample.app.database.dashboard.DashboardDao
import com.android.sample.app.database.dashboard.DbDashboard
import com.android.sample.app.database.dashboard.DbLinkConverter
import com.android.sample.app.database.section.DbSection
import com.android.sample.app.database.section.SectionDao

@Database(entities = [DbDashboard::class, DbSection::class], version = 1, exportSchema = false)
@TypeConverters(DbLinkConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dashboardDao(): DashboardDao
    abstract fun sectionDao(): SectionDao
}