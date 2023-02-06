package com.JpaPractice.order.service;

import com.JpaPractice.coffee.service.CoffeeService;
import com.JpaPractice.exception.BusinessLogicException;
import com.JpaPractice.exception.ExceptionCode;
import com.JpaPractice.helper.StampCalculator;
import com.JpaPractice.member.entity.Member;
import com.JpaPractice.member.service.MemberService;
import com.JpaPractice.order.entity.Order;
import com.JpaPractice.order.repository.OrderRepository;
import com.JpaPractice.stamp.Stamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private final MemberService memberService;
    private final OrderRepository orderRepository;
    private final CoffeeService coffeeService;

    public OrderService(MemberService memberService,
                        OrderRepository orderRepository,
                        CoffeeService coffeeService) {
        this.memberService = memberService;
        this.orderRepository = orderRepository;
        this.coffeeService = coffeeService;
    }

    public Order createOrder(Order order){
        verifyOrder(order);
        Order savedOrder = saveOrder(order);
        updateStamp(savedOrder);

        return savedOrder;
    }

    public Order updateOrder(Order order){
        Order findOrder = findVerifiedOrder(order.getOrderId());

        Optional.ofNullable(order.getOrderStatus())
                .ifPresent(orderStatus -> findOrder.setOrderStatus(orderStatus));
        return orderRepository.save(findOrder);
    }

    public Order findOrder(long orderId){
        return findVerifiedOrder(orderId);
    }

    public Page<Order> findOrders(int page, int size){
        return orderRepository.findAll(PageRequest.of(page,size,
                Sort.by("orderId").descending()));
    }

    public void cancelOrder(long orderId){
        Order findOrder = findVerifiedOrder(orderId);
        int step = findOrder.getOrderStatus().getStepNumber();

        if (step >= 2){
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ORDER);
        }
        findOrder.setOrderStatus(Order.OrderStatus.ORDER_CANCEL);
        orderRepository.save(findOrder);
    }

    private Order findVerifiedOrder(long orderId){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order findOrder =
                optionalOrder.orElseThrow(()->
                        new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
        return findOrder;
    }

    private void verifyOrder(Order order){
        memberService.findVerifiedMember(order.getMember().getMemberId());

        order.getOrderCoffees().stream()
                .forEach(orderCoffee -> coffeeService.
                        findVerifiedCoffee(orderCoffee.getCoffee().getCoffeeId()));
    }

    private void updateStamp(Order order){
        Member member = memberService.findMember(order.getMember().getMemberId());
        int earnedStampCount = StampCalculator.calculateEarnedStampCount(order);

        Stamp stamp = member.getStamp();
        stamp.setStampCount(
                StampCalculator.calculateStampCount(stamp.getStampCount(),
                        earnedStampCount));

        member.setStamp(stamp);

        memberService.updateMember(member);
    }

    private int calculateStampCount(Order order){
        return order.getOrderCoffees().stream()
                .map(orderCoffee -> orderCoffee.getQuantity())
                .mapToInt(quantity -> quantity)
                .sum();
    }

    private Order saveOrder(Order order){ return orderRepository.save(order);}
}
