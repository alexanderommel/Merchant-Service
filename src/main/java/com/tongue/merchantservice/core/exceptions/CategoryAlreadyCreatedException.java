package com.tongue.merchantservice.core.exceptions;

public class CategoryAlreadyCreatedException extends RuntimeException{

    public CategoryAlreadyCreatedException(String name){
        super(String.format("Category with name %s already created",name));
    }

}
