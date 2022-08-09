package com.tongue.merchantservice.core.exceptions;

public class WrongPasswordException extends RuntimeException{

    public WrongPasswordException(){
        super(String.format("Wrong password"));
    }

}
