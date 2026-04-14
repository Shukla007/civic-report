package com.civicreport.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ReportEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CivicReportDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
}

