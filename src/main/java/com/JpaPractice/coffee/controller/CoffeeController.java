package com.JpaPractice.coffee.controller;

import com.JpaPractice.coffee.dto.CoffeePatchDto;
import com.JpaPractice.coffee.dto.CoffeePostDto;
import com.JpaPractice.coffee.entity.Coffee;
import com.JpaPractice.coffee.mapper.CoffeeMapper;
import com.JpaPractice.coffee.service.CoffeeService;
import com.JpaPractice.dto.MultiResponseDto;
import com.JpaPractice.dto.SingleResponseDto;
import com.JpaPractice.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v0/coffees")
@Validated
public class CoffeeController {
    private final static String COFFEE_DEFAULT_URL = "/v0/coffees";
    private CoffeeService coffeeService;
    private CoffeeMapper mapper;

    public CoffeeController(CoffeeService coffeeService, CoffeeMapper mapper) {
        this.coffeeService = coffeeService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postCoffee(@Valid @RequestBody CoffeePostDto coffeePostDto){
        //TODO post 리퀘스트(korName,engName,price,coffeeCode) -> 리스폰스
        Coffee coffee = coffeeService.createCoffee(mapper.coffeePostDtoToCoffee(coffeePostDto));
        URI location = UriCreator.createUri(COFFEE_DEFAULT_URL, coffee.getCoffeeId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{coffee-id}")
    public ResponseEntity patchCoffee(@PathVariable("coffee-id") @Positive long coffeeId,
                                      @Valid @RequestBody CoffeePatchDto coffeePatchDto){
        //TODO patch 리퀘스트(coffeeId, 바꿀 정보) -> 정보 조회 -> 리스폰스
        coffeePatchDto.setCoffeeId(coffeeId);
        Coffee coffee = coffeeService.updateCoffee(mapper.coffeePatchDtoToCoffee(coffeePatchDto));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.coffeeToCoffeeResponseDto(coffee)),
                HttpStatus.OK);
    }

    @GetMapping("/{coffee-id}")
    public ResponseEntity getCoffee(@PathVariable("coffee-id")@Positive long coffeeId){
        //TODO get 리퀘스트(coffeeId) -> 리스폰스
        Coffee coffee = coffeeService.findCoffee(coffeeId);

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.coffeeToCoffeeResponseDto(coffee)),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getCoffees(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size){
        //TODO getAll 리퀘스트 -> 리스폰스(페이지네이션 필요)
        Page<Coffee> pageCoffees = coffeeService.findCoffees(page-1, size);
        List<Coffee> coffees = pageCoffees.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.coffeesToCoffeeResponseDto(coffees),pageCoffees),
                HttpStatus.OK);
    }

    @DeleteMapping("/{coffee-id}")
    public ResponseEntity deleteCoffee(@PathVariable("coffee-id")@Positive long coffeeId){
        //TODO delete 리퀘스트(coffeeId) -> 리스폰스(서비스단에서 delete 구현)
        coffeeService.deleteCoffee(coffeeId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
