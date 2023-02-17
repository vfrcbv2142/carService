package com.blankerdog.carService.controller;

import com.blankerdog.carService.dto.ClientDto;
import com.blankerdog.carService.dto.ClientTransformer;
import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.model.Client;
import com.blankerdog.carService.services.AccountService;
import com.blankerdog.carService.services.ClientService;
import com.blankerdog.carService.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.principal.id == #accountId")
    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<ClientDto>>> getAllByAccountId(@RequestParam Long accountId){
        List<EntityModel<ClientDto>> clientsDto = clientService.getClientsByAccountId(accountId).stream()
                .map(x -> ClientTransformer.convertToDto(x))
                .map(x -> toModel(x))
                .toList();
        return new ResponseEntity<>(CollectionModel.of(clientsDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @clientController.canAccessClient(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClientDto>> getById(@PathVariable long id){
        return new ResponseEntity<>(toModel(ClientTransformer.convertToDto(clientService.readById(id))), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<EntityModel<ClientDto>> postClient(@RequestBody ClientDto clientDto){
        Client client = ClientTransformer.convertToEntity(clientDto,
                accountService.readById(clientDto.getAccountId()),
                orderService.findAllByIds(clientDto.getOrdersIds()));
        return new ResponseEntity<>(toModel(ClientTransformer.convertToDto(clientService.create(client))), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @clientController.canAccessClient(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ClientDto>> putById(@RequestBody ClientDto clientDto, @PathVariable long id){
        Client client = ClientTransformer.convertToEntity(clientDto,
                accountService.readById(clientDto.getAccountId()),
                orderService.findAllByIds(clientDto.getOrdersIds()));
        return new ResponseEntity<>(toModel(ClientTransformer.convertToDto(clientService.update(client, id))), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @clientController.canAccessClient(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        clientService.delete(id);
        return new ResponseEntity<String>("Client deleted successfully", HttpStatus.OK);
    }

    private static EntityModel<ClientDto> toModel(ClientDto clientDto){
        return EntityModel.of(clientDto,
                linkTo(methodOn(ClientController.class).getById(clientDto.getId())).withSelfRel(),
                linkTo(methodOn(ClientController.class).getAllByAccountId(clientDto.getAccountId())).withRel("clients"));
    }

    public boolean canAccessClient(long clientId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account =  (Account) authentication.getPrincipal();
        return clientService.getClientsByAccountId(account.getId()).stream()
                .map(x -> x.getId())
                .anyMatch(x -> x.equals(clientId));
    }

}
