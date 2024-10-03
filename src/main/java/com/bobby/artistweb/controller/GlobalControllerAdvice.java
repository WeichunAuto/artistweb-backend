package com.bobby.artistweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    @ResponseStatus(HttpStatus.REQUEST_ENTITY_TOO_LARGE)
    public ResponseEntity<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        System.out.println("Max upload size exceeded.");
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.REQUEST_ENTITY_TOO_LARGE);
    }
}
