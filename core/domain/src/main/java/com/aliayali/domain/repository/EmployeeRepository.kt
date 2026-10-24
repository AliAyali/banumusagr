package com.aliayali.domain.repository

import com.aliayali.model.Employee
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {

    fun observeEmployee(): Flow<Employee?>

    suspend fun saveEmployee(employee: Employee)
}