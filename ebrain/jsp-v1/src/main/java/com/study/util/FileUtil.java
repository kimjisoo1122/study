package com.study.util;

import java.io.File;

public interface FileUtil {

    String FILE_PATH = "C:\\Users\\kimji\\IdeaProjects\\study\\ebrain\\jsp-v1\\src\\main\\webapp\\WEB-INF\\upload";
    String ENC_TYPE = "UTF-8";
    int BOARD_FILE_MAX_SIZE = 1024 * 1024 * 10;

    static File uploadedFile(String fileName) {
        return new File(FILE_PATH + File.separator + fileName);
    }
}
