package com.aliayali.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS report_period_state (
                periodType TEXT NOT NULL PRIMARY KEY,
                resetAtMillis INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}