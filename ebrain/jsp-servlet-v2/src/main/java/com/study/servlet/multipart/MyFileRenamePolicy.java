package com.study.servlet.multipart;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MultiPartRequest 생성시 파일 중복시 파일이름 생성 정책 클래스입니다.
 */
public class MyFileRenamePolicy implements FileRenamePolicy {

    /**
     * 파일이름이 중복될 경우 기존 파일이름에 현재날짜를 추가하여 생성합니다.
     * @param file
     * @return
     */
    @Override
    public File rename(File file) {
        String fileName = file.getName();
        int dotIdx = fileName.lastIndexOf(".");
        String fileExtension = fileName.substring(dotIdx);
        String extractFileName = fileName.substring(0, dotIdx);

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH_mmss"));
        fileName = extractFileName + "_" + now + fileExtension;

        return new File(file.getParent(), fileName);
    }
}
