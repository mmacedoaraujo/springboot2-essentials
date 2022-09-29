package com.mmacedo.springboot2essentials.handler;

import com.mmacedo.springboot2essentials.exceptions.BadRequestException;
import com.mmacedo.springboot2essentials.exceptions.BadRequestExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException badReqEx) {
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad request exception, check the documentation")
                        .details(badReqEx.getMessage())
                        .developerMessage(badReqEx.getClass().getName()).build(), HttpStatus.BAD_REQUEST);
    }
}
