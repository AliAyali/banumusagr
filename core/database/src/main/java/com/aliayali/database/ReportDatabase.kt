package com.aliayali.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aliayali.database.dao.EmployeeDao
import com.aliayali.database.dao.ReportPeriodStateDao
import com.aliayali.database.dao.WorkReportDao
import com.aliayali.database.dao.WorkTimeCountersDao
import com.aliayali.database.entity.EmployeeEntity
import com.aliayali.database.entity.ReportPeriodStateEntity
import com.aliayali.database.entity.WorkReportEntity
import com.aliayali.database.entity.WorkTimeCountersEntity

@Database(
    entities = [
        EmployeeEntity::class,
        WorkReportEntity::class,
        ReportPeriodStateEntity::class,
        WorkTimeCountersEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class ReportDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao

    abstract fun workReportDao(): WorkReportDao

    abstract fun reportPeriodStateDao(): ReportPeriodStateDao

    abstract fun workTimeCountersDao(): WorkTimeCountersDao

    companion object {
        @Volatile
        private var INSTANCE: ReportDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // منطق مهاجرت قبلی نسخه ۱ به ۲ را اینجا قرار بده.
                // اگر این مهاجرت از قبل تعریف شده، همان را نگه دار.
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `work_time_counters` (
                        `id` INTEGER NOT NULL,
                        `dailyMinutes` INTEGER NOT NULL DEFAULT 0,
                        `weeklyMinutes` INTEGER NOT NULL DEFAULT 0,
                        `monthlyMinutes` INTEGER NOT NULL DEFAULT 0,
                        `totalMinutes` INTEGER NOT NULL DEFAULT 0,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): ReportDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ReportDatabase::class.java,
                    "banumusagr_database"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3
                    )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}