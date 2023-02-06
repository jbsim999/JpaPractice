package com.JpaPractice.order.dto;

import com.JpaPractice.member.entity.Member;
import com.JpaPractice.order.entity.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    @Getter
    @AllArgsConstructor
    public static class Post{
        @Positive
        private long memberId;

        @Valid
        private List<OrderCoffeeDto> orderCoffees;

        public Member getMember(){
            Member member = new Member();
            member.setMemberId(memberId);
            return member;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Patch{
        private long orderId;
        private Order.OrderStatus orderStatus;

        public void setOrderId(long orderId){this.orderId = orderId;}
    }

    @Getter
    @Setter
    public static class Response{
        private long orderId;
        @Setter(AccessLevel.NONE)
        private long memberId;
        private Order.OrderStatus orderStatus;
        private List<OrderCoffeeResponseDto> orderCoffees;
        private LocalDateTime createdAt;

        public void setMember(Member member){ this.memberId = member.getMemberId();}
    }
}
