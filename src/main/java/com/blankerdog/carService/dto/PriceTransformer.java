package com.blankerdog.carService.dto;

import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.model.Price;

public class PriceTransformer {

    public static PriceDto convertToDto(Price price){
        return new PriceDto(
                price.getId(),
                price.getPainting(),
                price.getPreparingAluminum(),
                price.getPreparingPlastic(),
                price.getPreparingIron(),
                price.getSoldering(),
                price.getDisassembling(),
                price.getStraightening(),
                price.getAccount().getId()
        );
    }

    public static Price convertToEntity(PriceDto priceDto, Account account){
        return new Price(
                priceDto.getId(),
                priceDto.getPainting(),
                priceDto.getPreparingAluminum(),
                priceDto.getPreparingPlastic(),
                priceDto.getPreparingIron(),
                priceDto.getSoldering(),
                priceDto.getDisassembling(),
                priceDto.getStraightening(),
                account
        );
    }
}
