package com.study.exception;

import java.nio.file.FileSystemException;

public class FileUploadPathNotExistException extends FileSystemException {
    public FileUploadPathNotExistException() {
        super("파일 업로드 경로가 존재하지 않습니다.");
    }
}
