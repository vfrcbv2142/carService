package com.blankerdog.carService.controller;

import com.blankerdog.carService.dto.*;
import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.model.Item;
import com.blankerdog.carService.services.ItemService;
import com.blankerdog.carService.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/items")
public class ItemController {
    @Autowired
    ItemService itemService;
    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ItemDto>> getById(@PathVariable long id){
        return new ResponseEntity<>(toModel(ItemTransformer.convertToDto(itemService.readById(id))), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<EntityModel<ItemDto>> postItem(@RequestBody ItemDto itemDto){
        Item item = ItemTransformer.convertToEntity(
                itemDto,
                orderService.readById(itemDto.getOrderId())
        );
        return new ResponseEntity<>(toModel(ItemTransformer.convertToDto(itemService.create(item))), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @itemController.canAccessItem(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ItemDto>> putItem(@RequestBody ItemDto itemDto, @PathVariable long id){
        Item item = ItemTransformer.convertToEntity(
                itemDto,
                null
        );
        return new ResponseEntity<>(toModel(ItemTransformer.convertToDto(itemService.update(item, id))), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @itemController.canAccessItem(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable long id){
        itemService.delete(id);
        return new ResponseEntity<>("Item successfully deleted", HttpStatus.OK);
    }

    private static EntityModel<ItemDto> toModel(ItemDto itemDto){
        return EntityModel.of(itemDto,
                linkTo(methodOn(ItemController.class).deleteItem(itemDto.getId())).withSelfRel());
    }

    public boolean canAccessItem(long itemId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account =  (Account) authentication.getPrincipal();

        List<Long> itemsId = orderService.findAllByIds(orderService.findAllByAccountId(account.getId()).stream()
                        .map(x -> x.getId()).toList()).stream()
                            .flatMap(y -> y.getItems().stream()
                                    .map(z -> z.getId())
                            ).toList();
        return itemsId.stream().anyMatch(x -> x.equals(itemId));
    }
}
