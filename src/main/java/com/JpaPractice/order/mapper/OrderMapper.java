package com.JpaPractice.order.mapper;

import com.JpaPractice.coffee.entity.Coffee;
import com.JpaPractice.member.entity.Member;
import com.JpaPractice.order.dto.OrderCoffeeResponseDto;
import com.JpaPractice.order.dto.OrderDto;
import com.JpaPractice.order.entity.Order;
import com.JpaPractice.order.entity.OrderCoffee;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order orderPatchDtoToOrder(OrderDto.Patch orderPatchDto);

    List<OrderDto.Response> ordersToOrderResponseDtos(List<Order> orders);

    default Order orderPostDtoToOrder(OrderDto.Post orderPostDto){
        Order order = new Order();
        Member member = new Member();
        member.setMemberId(orderPostDto.getMemberId());

        List<OrderCoffee> orderCoffees = orderPostDto.getOrderCoffees().stream()
                .map(orderCoffeeDto -> {
                    OrderCoffee orderCoffee = new OrderCoffee();
                    Coffee coffee = new Coffee();
                    coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());
                    orderCoffee.addOrder(order);
                    orderCoffee.addCoffee(coffee);
                    orderCoffee.setQuantity(orderCoffeeDto.getQuantity());
                    return orderCoffee;
                }).collect(Collectors.toList());
        order.setMember(member);
        order.setOrderCoffees(orderCoffees);

        return order;
    }

    default OrderDto.Response orderToOrderResponseDto(Order order){
        List<OrderCoffee> orderCoffees = order.getOrderCoffees();

        OrderDto.Response orderResponseDto = new OrderDto.Response();
        orderResponseDto.setOrderId(order.getOrderId());
        orderResponseDto.setMember(order.getMember());
        orderResponseDto.setOrderStatus(order.getOrderStatus());
        orderResponseDto.setCreatedAt(order.getCreatedAt());
        orderResponseDto.setOrderCoffees(
                orderCoffeesToOrderCoffeeResponseDtos(orderCoffees)
        );

        return orderResponseDto;
    }

    default List<OrderCoffeeResponseDto> orderCoffeesToOrderCoffeeResponseDtos(
            List<OrderCoffee> orderCoffees){
        return orderCoffees
                .stream()
                .map(orderCoffee -> OrderCoffeeResponseDto
                        .builder()
                        .coffeeId(orderCoffee.getCoffee().getCoffeeId())
                        .quantity(orderCoffee.getQuantity())
                        .price(orderCoffee.getCoffee().getPrice())
                        .korName(orderCoffee.getCoffee().getKorName())
                        .engName(orderCoffee.getCoffee().getEngName())
                        .build())
                .collect(Collectors.toList());
    }
}
