package com.study.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public interface FileUtil {

    String FILE_PATH = "C:\\Users\\kimji\\IdeaProjects\\study\\ebrain\\jsp-servlet-v2\\src\\main\\webapp\\WEB-INF\\upload";
    String ENC_TYPE = "UTF-8";
    int BOARD_FILE_MAX_SIZE = 1024 * 1024 * 10;

    static File getUploadedFile(String fileName) {
        return new File(FILE_PATH + File.separator + fileName);
    }

    static boolean checkUploadPath() {
        File uploadFolder = new File(FileUtil.FILE_PATH);
        if (!uploadFolder.exists()) {
            return uploadFolder.mkdirs();
        }
        return true;
    }
}
