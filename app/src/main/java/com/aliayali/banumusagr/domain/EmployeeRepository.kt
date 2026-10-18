package com.aliayali.banumusagr.domain

import com.aliayali.banumusagr.domain.model.Employee
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {

    fun observeEmployee(): Flow<Employee?>

    suspend fun saveEmployee(employee: Employee)
}