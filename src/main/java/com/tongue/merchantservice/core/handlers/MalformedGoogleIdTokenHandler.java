package com.tongue.merchantservice.core.handlers;

import com.tongue.merchantservice.core.ApiResponse;
import com.tongue.merchantservice.core.exceptions.MalformedGoogleIdTokenException;
import com.tongue.merchantservice.core.exceptions.RucAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MalformedGoogleIdTokenHandler {

    @ResponseBody
    @ExceptionHandler(MalformedGoogleIdTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiResponse handler(MalformedGoogleIdTokenException exception){
        return ApiResponse.error(exception.getMessage());
    }

}
