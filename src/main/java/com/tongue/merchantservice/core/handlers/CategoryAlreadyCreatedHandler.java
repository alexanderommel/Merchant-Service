package com.tongue.merchantservice.core.handlers;

import com.tongue.merchantservice.core.ApiResponse;
import com.tongue.merchantservice.core.exceptions.CategoryAlreadyCreatedException;
import com.tongue.merchantservice.core.exceptions.MalformedGoogleIdTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CategoryAlreadyCreatedHandler {

    @ResponseBody
    @ExceptionHandler(CategoryAlreadyCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiResponse handler(CategoryAlreadyCreatedException exception){
        return ApiResponse.error(exception.getMessage());
    }

}
