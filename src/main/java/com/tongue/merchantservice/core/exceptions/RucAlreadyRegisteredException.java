package com.tongue.merchantservice.core.exceptions;

public class RucAlreadyRegisteredException extends RuntimeException{

    public RucAlreadyRegisteredException(){
        super("A store using this ruc has been already registered");
    }

}
