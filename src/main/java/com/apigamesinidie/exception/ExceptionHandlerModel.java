package com.apigamesinidie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class ExceptionHandlerModel extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponseModel> notFoundCustomException(WebRequest request, Exception e){
        ExceptionResponseModel response = new ExceptionResponseModel(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
                );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadRequestException.class )
    public ResponseEntity<ExceptionResponseModel> badRequestCustomException(WebRequest request, Exception e){
        ExceptionResponseModel response = new ExceptionResponseModel(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
