package com.study.util;

import java.io.File;

public interface FileUtil {

    String FILE_PATH = "C:\\Users\\kimji\\IdeaProjects\\study\\ebrain\\jsp-servlet-v2\\src\\main\\webapp\\WEB-INF\\upload";
    String ENC_TYPE = "UTF-8";
    int BOARD_FILE_MAX_SIZE = 1024 * 1024 * 10;

    /**
     * 업로드된 파일을 생성합니다.
     * @param fileName
     * @return
     */
    static File getUploadedFile(String fileName) {
        return new File(FILE_PATH + File.separator + fileName);
    }

    /**
     * 파일업로드 경로를 생성합니다.
     * @return
     */
    static boolean checkUploadPath() {
        File uploadFolder = new File(FileUtil.FILE_PATH);
        if (!uploadFolder.exists()) {
            return uploadFolder.mkdirs();
        }
        return true;
    }

    /**
     * 파일의 확장자를 추출합니다.
     * @param fileName
     * @return fileExtension 파일확장자
     */
    static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
