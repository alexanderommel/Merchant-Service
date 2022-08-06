package com.tongue.merchantservice.core.exceptions;

public class StoreNameAlreadyRegisteredException extends RuntimeException{

    public StoreNameAlreadyRegisteredException(String storeName){
        super(String.format("A store with name <%s> has been already created",storeName));
    }

}
