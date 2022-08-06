package com.tongue.merchantservice.core.handlers;

import com.tongue.merchantservice.core.ApiResponse;
import com.tongue.merchantservice.core.exceptions.RucAlreadyRegisteredException;
import com.tongue.merchantservice.core.exceptions.StoreNameAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StoreNameAlreadyRegisteredHandler {

    @ResponseBody
    @ExceptionHandler(StoreNameAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiResponse handler(StoreNameAlreadyRegisteredException exception){
        return ApiResponse.error(exception.getMessage());
    }

}
