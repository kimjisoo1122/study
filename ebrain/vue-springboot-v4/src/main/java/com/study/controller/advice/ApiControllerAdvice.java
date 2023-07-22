package com.study.controller.advice;

import com.study.api.ResponseApiStatus;
import com.study.api.ResponseDto;
import com.study.api.ResponseValidFormDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.FileNotFoundException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto> apiExceptionResponse(Exception ex) {

        log.error("API 에러발생 : {}", ex.getMessage());

        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseApiStatus.FAIL);
        response.setErrorMessage(ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    /**
     * 업로드파일의 사이즈크기 예외를 처리하는 핸들러입니다.
     * @param ex MaxUploadSizeExceededException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseValidFormDto> maxUploadSizeExHandler(MaxUploadSizeExceededException ex) {

        log.error("파일사이즈 에러발생 : {}", ex.getMessage());

        ResponseValidFormDto response = new ResponseValidFormDto();
        response.setStatus(ResponseApiStatus.FAIL);
        response.setErrorMessage(ex.getMessage());
        response.setErrorFields(Map.of("files", "파일사이즈가 유효하지 않습니다."));
        return ResponseEntity
                .badRequest()
                .body(response);
    }

    /**
     * 파일다운로드 예외를 처리하는 핸들러입니다.
     * @param ex FileNotFoundException
     */
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ResponseDto> fileNotFoundHandlerExHandler(FileNotFoundException ex) {

        log.error("API 다운로드 에러발생 : {}", ex.getMessage());

        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseApiStatus.FAIL);
        response.setErrorMessage(ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(response);
    }
}
