package com.tongue.merchantservice.core.exceptions;

public class StoreNotFoundException extends RuntimeException{

    public StoreNotFoundException(){
        super(String.format("No Store found"));
    }

}
