package com.JpaPractice.coffee.mapper;

import com.JpaPractice.coffee.dto.CoffeePatchDto;
import com.JpaPractice.coffee.dto.CoffeePostDto;
import com.JpaPractice.coffee.dto.CoffeeResponseDto;
import com.JpaPractice.coffee.entity.Coffee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoffeeMapper {

    Coffee coffeePostDtoToCoffee(CoffeePostDto coffeePostDto);

    Coffee coffeePatchDtoToCoffee(CoffeePatchDto coffeePatchDto);

    CoffeeResponseDto coffeeToCoffeeResponseDto(Coffee coffee);

    List<CoffeeResponseDto> coffeesToCoffeeResponseDto(List<Coffee> coffees);
}
