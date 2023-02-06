package com.JpaPractice.member.dto;

import com.JpaPractice.member.entity.Member;
import com.JpaPractice.stamp.Stamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MemberDto {

    @Getter
    @AllArgsConstructor
    public static class Post{
        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String password;

        @NotBlank
        private String name;

        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
                message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다.")
        private String phone;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch{
        private long memberId;

        @NotBlank
        private String name;

        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
                message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다")
        private String phone;

        private Member.MemberStatus memberStatus;

        public void setMemberId(long memberId){
            this.memberId = memberId;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class Response{
        private long memberId;
        private String email;
        private String name;
        private String phone;
        private Member.MemberStatus memberStatus;
        private Stamp stamp;

        public String getMemberStatus(){
            return memberStatus.getStatus();
        }

        public int getStamp(){
            return stamp.getStampCount();
        }
    }
}
