package com.study.util;

import java.io.File;

/**
 * 파일과 관련된 유틸을 제공합니다.
 */
public interface FileUtil {

    String FILE_PATH = "C:\\Users\\kimji\\IdeaProjects\\study\\ebrain\\jsp-servlet-v2\\src\\main\\webapp\\WEB-INF\\upload";
    String ENC_TYPE = "UTF-8";
    int BOARD_FILE_MAX_SIZE = 1024 * 1024 * 10;

    /**
     * 업로드경로의 파일을 생성합니다.
     * @param fileName
     * @return
     */
    static File createFile(String fileName) {
        return new File(FILE_PATH + File.separator + fileName);
    }

    /**
     * 파일업로드 경로를 생성합니다.
     * @return 경로가 존재하지 않으면 생성후 TRUE를 반환합니다.
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

    /**
     * multipartrequest로 부터 업로드된 파일들을 삭제합니다.
     * @param multi
     */
//    static void deleteFromMulti(MultipartRequest multi) {
//        Enumeration fileInputNames = multi.getFileNames();
//        while (fileInputNames.hasMoreElements()) {
//            String fileInputName = (String) fileInputNames.nextElement();
//            String physicalName = multi.getFilesystemName(fileInputName);
//            if (physicalName != null) {
//                File uploadedFile = multi.getFile(fileInputName);
//                if (uploadedFile.exists()) {
//                    uploadedFile.delete();
//                }
//            }
//        }
//    }

//    /**
//     * multipartrequest로 부터 첨부파일 리스트를 생성해서 반환합니다.
//     * @param multi
//     * @return List<FileDto> 게시글에 등록된 첨부파일 리스트
//     */
//    static List<FileDto> getFilesFromMulti(MultipartRequest multi) {
//        List<FileDto> files = new ArrayList<>();
//        Enumeration fileInputNames = multi.getFileNames();
//        while (fileInputNames.hasMoreElements()) {
//            String fileInputName = (String) fileInputNames.nextElement();
//            String fileName = multi.getFilesystemName(fileInputName);
//            String originalFileName = multi.getOriginalFileName(fileInputName);
//            if (fileName != null) {
//                File uploadedFile = multi.getFile(fileInputName);
//                String fileExtension = FileUtil.getFileExtension(fileName);
//                long fileSize = uploadedFile.length();
//
//                // 기존 업로드된 한글 파일명을 현재날짜로 변경
//                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmdd"));
//                String newName = now + "." + fileExtension;
//                uploadedFile.renameTo(FileUtil.createFile(newName));
//
//                // 파일 DTO 생성
//                FileDto fileDto = new FileDto();
//                fileDto.setPhysicalName(newName);
//                fileDto.setPath(FileUtil.FILE_PATH);
//                fileDto.setOriginalName(originalFileName);
//                fileDto.setFileSize(fileSize);
//                fileDto.setFileExtension(fileExtension);
//
//                files.add(fileDto);
//            }
//        }
//        return files;
//    }
}
