package com.study.service;

import com.study.dto.FileDto;
import com.study.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 첨부파일의 비즈니스로직을 처리하는 서비스 입니다.
 */
@Service
public class FileService {

    private final String filePath; // 업로드 파일경로
    private final FileRepository fileRepository;

    public FileService(@Value("${file.upload-folder}") String filePath, FileRepository fileRepository) {
        this.filePath = filePath;
        this.fileRepository = fileRepository;
        File uploadFolder = new File(filePath);
        uploadFolder.mkdirs();
    }

    /**
     * 첨부파일을 등록합니다
     * @param fileDto
     * @return fileId 등록된 파일 번호
     */
    @Transactional
    public Long save(FileDto fileDto) {
        fileRepository.insert(fileDto);
        return fileDto.getFileId();
    }

    /**
     * 게시글번호로 첨부파일을 조회합니다.
     * @param boardId 게시글번호
     * @return List<FileDto> 파일정보 리스트
     */
    public List<FileDto> findAllByBoardId(Long boardId) {
        return fileRepository.selectByBoardId(boardId);
    }

    /**
     * 첨부파일을 조회합니다.
     * @param fileId
     * @return FileDto 조회한 파일정보 DTO
     */
    public FileDto findByFileId(Long fileId) {
        return fileRepository.selectByFileId(fileId);
    }

    /**
     * 첨부파일과 업로드된 실제파일도 삭제합니다
     * @param fileId 파일번호
     * @return 삭제 행 갯수
     */
    @Transactional
    public int delete(Long fileId) {
        FileDto findFile = fileRepository.selectByFileId(fileId);
        File uploadedFile = new File(findFile.getFullPath());
        if (uploadedFile.exists()) {
            uploadedFile.delete();
        }

        return fileRepository.delete(fileId);
    }

    /**
     * Form에서 입력받은 멀티파트파일을 실제 파일로 업로드하고 DTO를 반환합니다.
     * @param multipartFiles 폼에서 전송된 멀티파트파일
     * @return List<FileDto> 파일 DB에 저장하는 DTO 리스트
     * @throws IOException
     */
    public List<FileDto> createFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<FileDto> files = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            if (file.isEmpty()) {
                continue;
            }

            // 업로드경로에 파일 저장
            String fileName = file.getOriginalFilename();
            String ext = extractExtension(fileName);
            String formattedFileName = getFormattedFileName(ext);
            file.transferTo(new File(filePath + formattedFileName));

            // 파일 DB 데이터 생성
            FileDto saveFile = new FileDto();
            saveFile.setPhysicalName(formattedFileName);
            saveFile.setOriginalName(file.getOriginalFilename());
            saveFile.setFileExtension(ext);
            saveFile.setFileSize(file.getSize());
            saveFile.setPath(filePath);
            files.add(saveFile);
        }
        return files;
    }

    /**
     * 포맷된 파일명을 반환합니다.
     * @param ext 파일확장자
     * @return formattedFileNam e
     */
    private String getFormattedFileName(String ext) {
        return UUID.randomUUID() + "." + ext;
    }

    /**
     * 파일의 확장자명을 추출합니다.
     * @param fileName 파일이름
     * @return fileExntension 파일확장자
     */
    private String extractExtension(String fileName) {
        int dotIdx = fileName.lastIndexOf(".");
        return fileName.substring(dotIdx + 1);
    }

    /**
     * 파일의 업로드경로를 반환합니다.
     * @return filePath
     */
    public String getFilePath() {
        return filePath;
    }
}
