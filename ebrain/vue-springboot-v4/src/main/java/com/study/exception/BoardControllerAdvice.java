package com.study.exception;

import com.study.api.ResponseDto;
import com.study.api.ResponseStatus;
import com.study.api.ResponseValidFormDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;

//TODO 예외처리
@ControllerAdvice
public class BoardControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto> apiExceptionResponse(Exception ex) {
        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseStatus.FAIL);
        response.setErrorMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseValidFormDto> maxUploadSizeExceptionHandler(MaxUploadSizeExceededException e) {
        ResponseValidFormDto response = new ResponseValidFormDto();
        response.setStatus(ResponseStatus.FAIL);
        response.setErrorMessage("파일사이즈가 유효하지 않습니다.");
        response.setErrorFields(Map.of("files", "파일사이즈가 유효하지 않습니다."));
        return ResponseEntity
                .badRequest()
                .body(response);
    }
}
