package com.study.service;

import com.study.dto.FileDto;
import com.study.mapper.FileMapper;
import com.study.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 첨부파일의 비즈니스로직을 처리하는 서비스 입니다.
 */
@Service
@Transactional
public class FileService {

    private final String filePath;
    private final FileMapper fileMapper;

    public FileService( @Value("${file.save-folder}") String filePath, FileMapper fileMapper) {
        this.filePath = filePath;
        this.fileMapper = fileMapper;
    }

    /**
     * 멀티파트파일을 실제 파일로 업로드하고 DTO를 반환합니다.
     * @param multipartFiles 폼에서 전송된 멀티파트파일
     * @return List<FileDto> 파일 DTO
     * @throws IOException
     */
    public List<FileDto> createFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<FileDto> files = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            if (file.isEmpty()) {
                continue;
            }
            String fileName = file.getOriginalFilename();
            String ext = extractExtension(fileName);
            String formattedFileName = getFormattedFileName(ext);
            file.transferTo(new File(filePath + formattedFileName));
            // 저장성공

            // 파일 db저장
            FileDto fileDto = new FileDto();
            fileDto.setPhysicalName(formattedFileName);
            fileDto.setOriginalName(file.getOriginalFilename());
            fileDto.setFileExtension(ext);
            fileDto.setFileSize(file.getSize());
            fileDto.setPath(filePath);
            files.add(fileDto);
        }
        return files;
    }



    /**
     * 첨부파일을 등록합니다
     * @param fileDto
     * @return fileId
     */
    public Long save(FileDto fileDto) {
        fileMapper.insert(fileDto);
        return fileDto.getFileId();
    }

    /**
     * 게시글번호로 첨부파일을 조회합니다.
     * @param boardId
     * @return List<FileDto>
     */
    @Transactional(readOnly = true)
    public List<FileDto> findByBoardId(Long boardId) {
        return fileMapper.selectByBoardId(boardId);
    }


    /**
     * 첨부파일을 조회합니다.
     * @param fileId
     * @return
     */
    @Transactional(readOnly = true)
    public FileDto findById(Long fileId) {
        return fileMapper.selectById(fileId);
    }

    /**
     * 첨부파일과 업로드된 실제파일도 삭제합니다
     * @param fileId
     * @return 삭제행 갯수
     */
    public int delete(Long fileId) {
        int deletedRow = 0;
        FileDto findFile = fileMapper.selectById(fileId);
        File uploadedFile = new File(findFile.getPath() + findFile.getPhysicalName());
        if (uploadedFile.exists()) {
            if (uploadedFile.delete()) {
                deletedRow = fileMapper.delete(fileId);
            }
        }

        return deletedRow;
    }



    /**
     * 현재날짜로 포맷된 파일명을 반환합니다.
     * @param ext
     * @return formattedFileName
     */
    private String getFormattedFileName(String ext) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmdd"));
        return now + "." + ext;
    }

    /**
     * 파일의 확장자명을 추출합니다.
     * @param fileName
     * @return fileExntension
     */
    private String extractExtension(String fileName) {
        int dotIdx = fileName.lastIndexOf(".");
        return fileName.substring(dotIdx + 1);
    }
}
