package com.study.exception;

import com.study.api.ResponseDto;
import com.study.api.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BoardControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto> apiExceptionResponse(Exception ex) {
        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseStatus.FAIL);
        response.setErrorMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
