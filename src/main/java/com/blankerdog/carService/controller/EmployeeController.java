package com.blankerdog.carService.controller;

import com.blankerdog.carService.dto.EmployeeDto;
import com.blankerdog.carService.dto.EmployeeTransformer;
import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.model.Employee;
import com.blankerdog.carService.services.AccountService;
import com.blankerdog.carService.services.EmployeeService;
import com.blankerdog.carService.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    AccountService accountService;
    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.principal.id == #accountId")
    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<EmployeeDto>>> getAllByAccountId(@RequestParam long accountId){
        List<EntityModel<EmployeeDto>> employeesDto =  employeeService.getEmployeesByAccountId(accountId).stream()
                .map(x -> EmployeeTransformer.convertToDto(x))
                .map(x -> toModel(x))
                .toList();
        return new ResponseEntity<>(CollectionModel.of(employeesDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @employeeController.canAccessEmployee(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EmployeeDto>> getById(@PathVariable long id){
        return new ResponseEntity<>(toModel(EmployeeTransformer.convertToDto(employeeService.readById(id))), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<EntityModel<EmployeeDto>> postClient(@RequestBody EmployeeDto employeeDto){
        Employee employee = EmployeeTransformer.convertToEntity(employeeDto,
                accountService.readById(employeeDto.getAccountId()),
                orderService.findAllByIds(employeeDto.getOrdersIds()));
        return new ResponseEntity<>(toModel(EmployeeTransformer.convertToDto(employeeService.create(employee))), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @employeeController.canAccessEmployee(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EmployeeDto>> putById(@RequestBody EmployeeDto employeeDto, @PathVariable long id){
        Employee employee = EmployeeTransformer.convertToEntity(employeeDto,
                accountService.readById(employeeDto.getAccountId()),
                orderService.findAllByIds(employeeDto.getOrdersIds()));
        return new ResponseEntity<>(toModel(EmployeeTransformer.convertToDto(employeeService.update(employee, id))), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @employeeController.canAccessEmployee(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        employeeService.delete(id);
        return new ResponseEntity<String>("Employee deleted successfully", HttpStatus.OK);
    }

    private static EntityModel<EmployeeDto> toModel(EmployeeDto employeeDto){
        return EntityModel.of(employeeDto,
                linkTo(methodOn(EmployeeController.class).getById(employeeDto.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllByAccountId(employeeDto.getAccountId())).withRel("employees"));
    }

    public boolean canAccessEmployee(long employeeId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account =  (Account) authentication.getPrincipal();
        return employeeService.getEmployeesByAccountId(account.getId()).stream()
                .map(x -> x.getId())
                .anyMatch(x -> x.equals(employeeId));
    }
}
