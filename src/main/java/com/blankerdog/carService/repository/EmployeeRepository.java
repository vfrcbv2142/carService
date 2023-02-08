package com.blankerdog.carService.repository;

import com.blankerdog.carService.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> getEmployeesByAccountId(long accountId);
}
