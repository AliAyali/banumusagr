package com.aliayali.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee")
data class EmployeeEntity(
    @PrimaryKey
    val id: Int = 1,
    val firstName: String,
    val lastName: String,
    val department: String
)