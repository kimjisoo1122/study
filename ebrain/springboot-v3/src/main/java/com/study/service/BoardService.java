package com.study.service;

import com.study.dto.*;
import com.study.mapper.BoardMapper;
import com.study.mapper.FileMapper;
import com.study.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

/**
 * 게시글 관련된 비즈니스 로직을 처리하는 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    private final FileMapper fileMapper;
    private final FileService fileService;

    /**
     * 게시글을 등록합니다.
     * 첨부파일이 존재하면 첨부파일 등록
     * @param form 업데이트용 Board Form
     * @return
     */
    public Long register(BoardRegisterForm form) throws IOException {
        BoardDto boardDto = createRegisterBoard(form);

        boardMapper.insert(boardDto);
        Long boardId = boardDto.getBoardId();

        // 파일을 생성하고 첨부파일을 등록합니다.
        List<FileDto> files = fileService.createFiles(form.getFiles());
        for (FileDto file : files) {
            file.setBoardId(boardId);
            fileMapper.insert(file);
        }

        return boardId;
    }

    /**
     * 게시글을 조회합니다.
     * @param boardId
     * @return BoardDto
     */
    @Transactional(readOnly = true)
    public BoardDto findById(Long boardId) {
        return boardMapper.selectById(boardId);
    }

    /**
     * 게시글을 검색조건에 맞게 조회합니다.
     * @param condition 검색조건
     * @return
     */
    @Transactional(readOnly = true)
    public List<BoardDto> findByCondition(BoardSearchCondition condition) {
        return boardMapper.selectByCondition(condition);
    }

    /**
     * 검색조건에 해당하는 게시글 총 갯수를 조회합니다
     * @param condition 검색조건
     * @return 게시글 총 갯수
     */
    @Transactional(readOnly = true)
    public int getTotalSize(BoardSearchCondition condition) {
        return boardMapper.countByCondition(condition);
    }

    /**
     * 게시글을 수정합니다.
     * 첨부파일이 존재하면 추가합니다.
     * 삭제하필 파일이 존재하면 삭제합니다.
     * @param form 업데이트용 Form
     * @return 업데이트 행 갯수
     */
    public int update(BoardUpdateForm form) throws IOException {
        // 첨부파일이 있는 경우 추가합니다.
        List<FileDto> files = fileService.createFiles(form.getFiles());
        for (FileDto file : files) {
            file.setBoardId(form.getBoardId());
            fileMapper.insert(file);
        }

        // 삭제파일이 있는 경우 삭제합니다.
        String[] filedIds = form.getFileIds();
        if (filedIds != null) {
            for (String fileId : filedIds) {
                fileService.delete(Long.valueOf(fileId));
            }
        }

        BoardDto updateBoard = createUpdateBoard(form);

        return boardMapper.update(updateBoard);
    }

    /**
     * 조회수를 1 증가시킵니다.
     * @param boardId
     */
    public void increaseViewCnt(Long boardId) {
        boardMapper.increaseViewCnt(boardId);
    }

    /**
     * 게시글과 관련된 댓글, 첨부파일을 전부 삭제합니다.
     * 첨부된 실제 파일이 존재하면 삭제합니다.
     * @param boardId
     * @return 삭제행 갯수
     */
    public int delete(Long boardId) {
        // 업로드된 실제파일을 삭제합니다.
        List<FileDto> files = fileMapper.selectByBoardId(boardId);
        for (FileDto file : files) {
            File uploadedFile = FileUtil.createFile(file.getPhysicalName());
            if (uploadedFile.exists()) {
                uploadedFile.delete();
            }
        }

        return boardMapper.delete(boardId);
    }

    /**
     * 비밀번호를 암호화 합니다
     * @param password
     * @return 암호화된 비밀번호
     */
    public String encryptPwd(String password) {
        if (password == null) {
            return null;
        }

        String encryptedPwd = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

            StringBuilder sb = new StringBuilder();

            for (byte byteDatum : md.digest()) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }

            encryptedPwd = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedPwd;
    }

    /**
     * 비밀번호를 검증합니다.
     * @param boardId 게시글 번호
     * @param password 입력받은 패스워드
     * @return
     */
    public boolean isPasswordMatch(Long boardId, String password) {
        String findPassword = Optional.ofNullable(boardMapper.selectById(boardId))
                .map(BoardDto::getPassword)
                .orElse("");
        String encryptedPassword = encryptPwd(password);
        return findPassword.equals(encryptedPassword);
    }

    /**
     * 게시글 등록에 사용하는 Dto를 생성합니다.
     * @param form 등록 Form
     * @return BoardDto 등록 Dto
     */
    private BoardDto createRegisterBoard(BoardRegisterForm form) {
        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(form.getCategoryId());
        boardDto.setWriter(form.getWriter());
        boardDto.setTitle(form.getTitle());
        boardDto.setContent(form.getContent());

        String encryptedPwd = encryptPwd(form.getPassword());
        boardDto.setPassword(encryptedPwd);

        return boardDto;
    }
    /**
     * 게시글 업데이트에 사용하는 Dto를 생성합니다.
     * @param form 업데이트 Form
     * @return BoardDto 업데이트 Dto
     */
    private BoardDto createUpdateBoard(BoardUpdateForm form) {
        BoardDto boardDto = new BoardDto();
        boardDto.setCategoryId(form.getCategoryId());
        boardDto.setWriter(form.getWriter());
        boardDto.setTitle(form.getTitle());
        boardDto.setContent(form.getContent());
        boardDto.setBoardId(form.getBoardId());

        return boardDto;
    }
}
