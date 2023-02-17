package com.blankerdog.carService.controller;


import com.blankerdog.carService.dto.PriceDto;
import com.blankerdog.carService.dto.PriceTransformer;
import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.model.Price;
import com.blankerdog.carService.services.AccountService;
import com.blankerdog.carService.services.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {
    @Autowired
    PriceService priceService;
    @Autowired
    AccountService accountService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.principal.id == #accountId")
    @GetMapping()
    public ResponseEntity<EntityModel<PriceDto>> getByAccountId(@RequestParam long accountId){
        return new ResponseEntity<>(toModel(PriceTransformer.convertToDto(priceService.getByAccountId(accountId))), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<EntityModel<PriceDto>> postPrice(@RequestBody PriceDto priceDto){
        Price price = PriceTransformer.convertToEntity(priceDto, accountService.readById(priceDto.getAccountId()));
        return new ResponseEntity<>(toModel(PriceTransformer.convertToDto(priceService.create(price))), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @priceController.canAccessPrice(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PriceDto>> putPrice(@RequestBody PriceDto priceDto, @PathVariable long id){
        Price price = PriceTransformer.convertToEntity(priceDto, accountService.readById(priceDto.getAccountId()));
        return new ResponseEntity<>(toModel(PriceTransformer.convertToDto(priceService.update(price, id))), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePrice(@PathVariable long id){
        priceService.delete(id);
        return new ResponseEntity<>("Price successfully deleted", HttpStatus.OK);
    }

    private static EntityModel<PriceDto> toModel(PriceDto priceDto){
        return EntityModel.of(priceDto,
                linkTo(methodOn(PriceController.class).getByAccountId(priceDto.getId())).withSelfRel());
    }

    public boolean canAccessPrice(long priceId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account =  (Account) authentication.getPrincipal();
        return priceService.readById(account.getId()).getId().equals(priceId);
    }
}
