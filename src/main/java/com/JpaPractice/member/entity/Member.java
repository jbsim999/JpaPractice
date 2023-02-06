package com.JpaPractice.member.entity;

import com.JpaPractice.order.entity.Order;
import com.JpaPractice.stamp.Stamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 13,nullable = false,unique = true)
    private String phone;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @OneToOne(mappedBy = "member",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Stamp stamp;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public void setOrder(Order order){
        orders.add(order);
        if (order.getMember() != this){
            order.setMember(this);
        }
    }

    public void setStamp(Stamp stamp){
        this.stamp = stamp;
        if (stamp.getMember() != this){
            stamp.setMember(this);
        }
    }

    public enum MemberStatus{
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status){
            this.status = status;
        }

    }
}
