package com.tongue.merchantservice.core.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException{

    public EmailAlreadyRegisteredException(){
        super(String.format("Email already registered"));
    }

}
