package com.tongue.merchantservice.core.exceptions;

public class MalformedGoogleIdTokenException extends RuntimeException{

    public MalformedGoogleIdTokenException(){
        super("Invalid Google Id Token");
    }

}
