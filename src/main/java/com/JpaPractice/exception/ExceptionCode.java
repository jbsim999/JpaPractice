package com.JpaPractice.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404,"Member not found"),
    MEMBER_EXISTS(404,"Member exists"),
    COFFEE_NOT_FOUND(404,"Coffee not found"),
    COFFEE_CODE_EXISTS(404,"Coffee Code exists"),
    ORDER_NOT_FOUND(404,"Order not found"),
    CANNOT_CHANGE_ORDER(404,"Order can not change"),
    NOT_IMPLEMETATION(404,"Not Implementation"),
    INVALID_MEMBER_STATUS(404,"Invalid member status");

    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int code, String message){
        this.status = code;
        this.message = message;
    }
}
