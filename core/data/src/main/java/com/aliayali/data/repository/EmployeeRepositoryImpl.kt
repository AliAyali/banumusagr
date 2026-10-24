package com.aliayali.data.repository

import com.aliayali.database.dao.EmployeeDao
import com.aliayali.database.entity.EmployeeEntity
import com.aliayali.domain.repository.EmployeeRepository
import com.aliayali.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDao: EmployeeDao,
) : EmployeeRepository {

    override fun observeEmployee(): Flow<Employee?> =
        employeeDao.observeEmployee().map { employee ->
            employee?.asModel()
        }

    override suspend fun saveEmployee(employee: Employee) {
        employeeDao.saveEmployee(employee.asEntity())
    }
}

private fun EmployeeEntity.asModel(): Employee =
    Employee(
        id = id,
        firstName = firstName,
        lastName = lastName,
        department = department,
    )

private fun Employee.asEntity(): EmployeeEntity =
    EmployeeEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        department = department,
    )