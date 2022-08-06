package com.tongue.merchantservice.core.handlers;

import com.tongue.merchantservice.core.ApiResponse;
import com.tongue.merchantservice.core.exceptions.EmailAlreadyRegisteredException;
import com.tongue.merchantservice.core.exceptions.RucAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RucAlreadyRegisteredHandler {

    @ResponseBody
    @ExceptionHandler(RucAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiResponse handler(RucAlreadyRegisteredException exception){
        return ApiResponse.error(exception.getMessage());
    }

}
