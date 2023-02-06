package com.blankerdog.carService.services;

import com.blankerdog.carService.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee readById(long id);
    Employee update(Employee employee, long id);
    void delete(long id);
    List<Employee> getAll();

    List<Employee> getEmployeesByAccountId(long accountId);
}
