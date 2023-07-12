package com.study.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.FileSystemException;

//@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "errorcode")
// responsertatus resolver가 sendError 로 상태코드랑 메시지 전달
public class FileUploadPathNotExistException extends FileSystemException {
    public FileUploadPathNotExistException() {
        super("파일 업로드 경로가 존재하지 않습니다.");
    }
}
