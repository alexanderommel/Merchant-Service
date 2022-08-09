package com.tongue.merchantservice.core.handlers;

import com.tongue.merchantservice.core.ApiResponse;
import com.tongue.merchantservice.core.exceptions.NoSuchEmailRegisteredException;
import com.tongue.merchantservice.core.exceptions.WrongPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WrongPasswordHandler {

    @ResponseBody
    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiResponse handler(WrongPasswordException exception){
        return ApiResponse.error(exception.getMessage());
    }

}
