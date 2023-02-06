package com.JpaPractice.coffee.dto;

import com.JpaPractice.coffee.entity.Coffee;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class CoffeePatchDto {

    private long coffeeId;

    @NotBlank
    private String korName;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z])(\\s?[A-Za-z])*$",
            message = "커피명(영문)은 영문이어야 합니다. 예) Cafe Latte")
    private String engName;

    @Range(min = 100, max = 50000)
    private Integer price;

    private Coffee.CoffeeStatus coffeeStatus;

    public void setCoffeeId(long coffeeId) {
        this.coffeeId = coffeeId;
    }
}
