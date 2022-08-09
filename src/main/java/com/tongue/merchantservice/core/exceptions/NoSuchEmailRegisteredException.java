package com.tongue.merchantservice.core.exceptions;

public class NoSuchEmailRegisteredException extends RuntimeException{

    public NoSuchEmailRegisteredException(){
        super(String.format("No such Merchant with that email"));
    }

}
