package com.JpaPractice.order.controller;

import com.JpaPractice.dto.MultiResponseDto;
import com.JpaPractice.dto.SingleResponseDto;
import com.JpaPractice.member.service.MemberService;
import com.JpaPractice.order.dto.OrderDto;
import com.JpaPractice.order.entity.Order;
import com.JpaPractice.order.mapper.OrderMapper;
import com.JpaPractice.order.service.OrderService;
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
@RequestMapping("/v0/orders")
@Validated
public class OrderController {
    private final static String ORDER_DEFAULT_URL = "/v11/orders";
    private final OrderService orderService;
    private final OrderMapper mapper;
    private final MemberService memberService;

    public OrderController(OrderService orderService, OrderMapper mapper, MemberService memberService) {
        this.orderService = orderService;
        this.mapper = mapper;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity postOrder(@Valid @RequestBody OrderDto.Post orderPostDto){
        //TODO post 리퀘스트(memberId,coffeeId,quantity) -> 리스폰스
        Order order = orderService.createOrder(mapper.orderPostDtoToOrder(orderPostDto));
        URI location = UriCreator.createUri(ORDER_DEFAULT_URL, order.getOrderId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{order-id}")
    public ResponseEntity patchOrder(@PathVariable("order-id") @Positive long orderId,
                                     @Valid @RequestBody OrderDto.Patch orderPatchDto){
        //TODO patch 리퀘스트(orderId, 바꿀 정보) -> 정보 조회 -> 리스폰스
        orderPatchDto.setOrderId(orderId);
        Order order =
                orderService.updateOrder(mapper.orderPatchDtoToOrder(orderPatchDto));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.orderToOrderResponseDto(order)),
                HttpStatus.OK);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity getOrder(@PathVariable("order-id") @Positive long orderId){
        //TODO get 리퀘스트(orderId) -> 리스폰스
        Order order = orderService.findOrder(orderId);

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.orderToOrderResponseDto(order)),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getOrders(@Positive @RequestParam int page,
                                    @Positive @RequestParam int size){
        //TODO getAll 리퀘스트 -> 리스폰스(페이지네이션 필요)
        Page<Order> pageOrders = orderService.findOrders(page-1, size);
        List<Order> orders = pageOrders.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.ordersToOrderResponseDtos(orders),pageOrders),
                HttpStatus.OK);
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity deleteOrder(@PathVariable("order-id") @Positive long orderId){
        //TODO delete 리퀘스트(orderId) -> 리스폰스(서비스단에서 delete 구현)
        orderService.cancelOrder(orderId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
