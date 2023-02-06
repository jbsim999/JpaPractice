package com.JpaPractice.coffee.entity;

import com.JpaPractice.order.entity.OrderCoffee;
import com.JpaPractice.stamp.Stamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Coffee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coffeeId;

    @Column(length = 100, nullable = false)
    private String korName;

    @Column(length = 100, nullable = false)
    private String engName;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 3, nullable = false, unique = true)
    private String coffeeCode;

    @OneToMany(mappedBy = "coffee")
    private List<OrderCoffee> orderCoffees = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private CoffeeStatus coffeeStatus = CoffeeStatus.COFFEE_FOR_SALE;

    public void addOrderCoffee(OrderCoffee orderCoffee){
        this.orderCoffees.add(orderCoffee);
        if (orderCoffee.getCoffee() != this){
            orderCoffee.addCoffee(this);
        }
    }

    public enum CoffeeStatus{
        COFFEE_FOR_SALE("판매중"),
        COFFEE_SOLD_OUT("판매중지");

        @Getter
        private String status;

        CoffeeStatus(String status){
            this.status = status;
        }
    }
}
