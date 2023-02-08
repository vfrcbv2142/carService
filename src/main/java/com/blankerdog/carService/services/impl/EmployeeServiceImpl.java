package com.blankerdog.carService.services.impl;

import com.blankerdog.carService.model.Employee;
import com.blankerdog.carService.repository.EmployeeRepository;
import com.blankerdog.carService.services.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        if (employee != null){
            return employeeRepository.save(employee);
        }
        throw new NullPointerException("Employee cannot be 'null'");
    }

    @Override
    public Employee readById(long id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Employee with id " + id + " not found"));
    }

    @Override
    public Employee update(Employee employee, long id) {
        Employee existingEmployee = readById(id);
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setPhoneNumber(employee.getPhoneNumber());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPosition(employee.getPosition());
        existingEmployee.setAccount(employee.getAccount());
        existingEmployee.setOrders(employee.getOrders());
        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void delete(long id) {
        employeeRepository.delete(readById(id));
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> getEmployeesByAccountId(long accountId){
        return employeeRepository.getEmployeesByAccountId(accountId);
    }
}
