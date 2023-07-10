package com.study.exception;

public class HandlerAdapterNotFoundException extends RuntimeException{

    public HandlerAdapterNotFoundException() {
        super("handler를 처리 할 adapter가 존재하지 않습니다");
    }
}
