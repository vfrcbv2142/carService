package com.blankerdog.carService.controller;

import com.blankerdog.carService.dto.OrderDto;
import com.blankerdog.carService.dto.OrderTransformer;
import com.blankerdog.carService.model.Order;
import com.blankerdog.carService.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    AccountService accountService;
    @Autowired
    ClientService clientService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ItemService itemService;
    @Autowired
    NoteService noteService;

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<OrderDto>>> getAllByAccountId(@RequestParam long accountId){
        List<EntityModel<OrderDto>> ordersDto = orderService.findAllByAccountId(accountId).stream()
                .map(x -> OrderTransformer.convertToDto(x))
                .map(x -> toModel(x))
                .toList();
        return new ResponseEntity<>(CollectionModel.of(ordersDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<OrderDto>> getById(@PathVariable long id){
        return new ResponseEntity<>(toModel(OrderTransformer.convertToDto(orderService.readById(id))), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<EntityModel<OrderDto>> postClient(@RequestBody OrderDto orderDto){
        Order order = OrderTransformer.convertToEntity(orderDto,
                accountService.readById(orderDto.getAccountId()),
                clientService.readById(orderDto.getClient().getId()),
                orderDto.getEmployees().stream()
                        .map(x -> employeeService.readById(x.getId()))
                        .collect(Collectors.toList()),
                orderDto.getItems().stream()
                        .map(x -> itemService.readById(x.getId()))
                        .collect(Collectors.toList())
                );
        return new ResponseEntity<>(toModel(OrderTransformer.convertToDto(orderService.create(order))), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<OrderDto>> putById(@RequestBody OrderDto orderDto, @PathVariable long id){
        Order order = OrderTransformer.convertToEntity(orderDto,
                accountService.readById(orderDto.getAccountId()),
                clientService.readById(orderDto.getClient().getId()),
                orderDto.getEmployees().stream()
                        .map(x -> employeeService.readById(x.getId()))
                        .collect(Collectors.toList()),
                orderDto.getItems().stream()
                        .map(x -> itemService.readById(x.getId()))
                        .collect(Collectors.toList())
        );
        return new ResponseEntity<>(toModel(OrderTransformer.convertToDto(orderService.update(order, id))), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        orderService.delete(id);
        return new ResponseEntity<String>("Order deleted successfully", HttpStatus.OK);
    }

    private static EntityModel<OrderDto> toModel(OrderDto orderDto){
        return EntityModel.of(orderDto,
                linkTo(methodOn(OrderController.class).getById(orderDto.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllByAccountId(orderDto.getAccountId())).withRel("orders"));
    }
}
