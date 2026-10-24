package com.aliayali.domain

import com.aliayali.domain.repository.EmployeeRepository
import com.aliayali.model.Employee
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmployeeUseCases @Inject constructor(
    private val repository: EmployeeRepository
) {
    fun observeEmployee(): Flow<Employee?> =
        repository.observeEmployee()

    suspend fun saveEmployee(employee: Employee) =
        repository.saveEmployee(employee)
}