package com.aliayali.banumusagr.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        EmployeeEntity::class,
        WorkReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ReportDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao

    abstract fun workReportDao(): WorkReportDao

    companion object {

        @Volatile
        private var INSTANCE: ReportDatabase? = null

        fun getDatabase(context: Context): ReportDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ReportDatabase::class.java,
                    "banumusagr_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}