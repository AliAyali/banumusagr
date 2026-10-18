package com.aliayali.banumusagr.database.repository

import com.aliayali.banumusagr.database.EmployeeDao
import com.aliayali.banumusagr.database.EmployeeEntity
import com.aliayali.banumusagr.domain.EmployeeRepository
import com.aliayali.banumusagr.domain.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EmployeeRepositoryImpl(
    private val employeeDao: EmployeeDao
) : EmployeeRepository {

    override fun observeEmployee(): Flow<Employee?> {
        return employeeDao.observeEmployee().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun saveEmployee(employee: Employee) {
        employeeDao.saveEmployee(employee.toEntity())
    }

    private fun EmployeeEntity.toDomain() = Employee(
        firstName = firstName,
        lastName = lastName,
        department = department
    )

    private fun Employee.toEntity() = EmployeeEntity(
        id = 1,
        firstName = firstName,
        lastName = lastName,
        department = department
    )
}