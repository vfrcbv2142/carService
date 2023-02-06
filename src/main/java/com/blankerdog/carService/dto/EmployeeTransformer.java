package com.blankerdog.carService.dto;

import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.model.Employee;
import com.blankerdog.carService.model.Order;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeTransformer {

    public static EmployeeDto convertToDto(Employee employee){
        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhoneNumber(),
                employee.getEmail(),
                employee.getPosition(),
                employee.getAccount().getId(),
                employee.getOrders().stream()
                        .map(x -> x.getId())
                        .collect(Collectors.toList())
        );
    }

    public static Employee convertToEntity(EmployeeDto employeeDto, Account account, List<Order> orders){
        return new Employee(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getPhoneNumber(),
                employeeDto.getEmail(),
                employeeDto.getPosition(),
                account,
                orders
        );
    }
}
