package com.JpaPractice.coffee.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class CoffeePostDto {
    @NotBlank
    private String korName;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z])(\\s?[A-Za-z])*$",
            message = "커피명(영문)은 영문이어야 합니다. 예) Cafe Latte")
    private String engName;

    @Range(min = 100,max = 50000)
    private int price;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z]){3}$",
            message = "커피 코드는 3자리 영문이어야 합니다.")
    private String coffeeCode;
}
