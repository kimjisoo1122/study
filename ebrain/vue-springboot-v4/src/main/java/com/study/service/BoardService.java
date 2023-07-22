package com.study.service;

import com.study.dto.*;
import com.study.repository.BoardRepository;
import com.study.repository.FileRepository;
import com.study.repository.ReplyRepository;
import com.study.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * 게시글 관련된 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
public class BoardService {

    private final FileService fileService;

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final ReplyRepository replyRepository;

    /**
     * 게시글을 등록합니다.
     * 첨부파일이 존재하면 첨부파일 등록
     * @param form 업데이트용 Board Form
     * @return boardId 등록된 게시글번호
     * @throws IOException 첨부파일의 생성에러
     */
    @Transactional()
    public Long register(BoardRegisterForm form) throws IOException {
        // 게시글을 등록하고 등록된 게시글번호를 반환합니다.
        BoardDto registerBoard = createRegisterBoard(form);
        boardRepository.insert(registerBoard);
        Long boardId = registerBoard.getBoardId();

        // 파일을 생성하고 첨부파일을 등록합니다.
        if (form.getFiles() != null) {
            List<FileDto> files = fileService.createFiles(form.getFiles());
            for (FileDto file : files) {
                file.setBoardId(boardId);
                fileRepository.insert(file);
            }
        }

        return boardId;
    }

    /**
     * 게시글을 조회합니다.
     * @param boardId
     * @return BoardDto 조회한 게시글 DTO
     */
    public BoardDto findByBoardId(Long boardId) {
        return boardRepository.selectByBoardId(boardId);
    }

    /**
     * 게시글을 검색조건에 맞게 조회합니다.
     * @param condition 검색조건
     * @return List<BoardDto> 게시글 목록
     */
    @Transactional(readOnly = true)
    public List<BoardDto> findAllByCondition(BoardSearchCondition condition) {
        return boardRepository.selectByCondition(condition);
    }

    /**
     * 검색조건에 해당하는 게시글 총 갯수를 조회합니다
     * @param condition 검색조건
     * @return 게시글 총 갯수
     */
    public int findBoardCnt(BoardSearchCondition condition) {
        return boardRepository.countByCondition(condition);
    }

    /**
     * 게시글을 수정합니다.
     * 첨부파일이 존재하면 추가합니다.
     * 삭제하필 파일이 존재하면 삭제합니다.
     * @param form 업데이트용 Form
     * @return 업데이트 행 갯수
     */
    @Transactional
    public int update(BoardUpdateForm form) throws IOException {
        // 첨부파일이 있는 경우 추가합니다.
        if (form.getFiles() != null) {
            List<FileDto> files = fileService.createFiles(form.getFiles());
            for (FileDto file : files) {
                file.setBoardId(form.getBoardId());
                fileRepository.insert(file);
            }
        }

        // 삭제파일이 있는 경우 삭제합니다.
        if (form.getDeleteFiles() != null) {
            for (Long fileId : form.getDeleteFiles()) {
                fileService.delete(fileId);
            }
        }

        return boardRepository.update(createUpdateBoard(form));
    }

    /**
     * 조회수를 1증가 시킵니다.
     * @param boardId 게시글 번호
     */
    public void increaseViewCnt(Long boardId) {
        boardRepository.increaseViewCnt(boardId);
    }

    /**
     * 게시글과 관련된 댓글, 첨부파일을 전부 삭제합니다.
     * 첨부된 실제 파일이 존재하면 삭제합니다.
     * @param boardId 게시글 번호
     * @return 삭제 행 갯수
     */
    @Transactional
    public int delete(Long boardId) {
        // 업로드 경로의 파일을 삭제하고 DB의 정보를 삭제합니다.
        List<FileDto> files = fileRepository.selectByBoardId(boardId);
        for (FileDto file : files) {
            File uploadedFile = new File(file.getFullPath());
            if (uploadedFile.exists()) {
                uploadedFile.delete();
            }
            fileRepository.delete(file.getFileId());
        }

        replyRepository.deleteByBoardId(boardId);

        // 파일 -> 댓글 -> 게시글순으로 삭제합니다.
        return boardRepository.delete(boardId);
    }



    /**
     * 비밀번호를 검증합니다.
     * @param boardId 게시글 번호
     * @param password 입력받은 원본패스워드
     * @return
     */
    public boolean isPasswordNotMatch(Long boardId, String password) {
        if (!StringUtils.hasText(password)) {
            return true;
        }

        String findPassword = Optional.ofNullable(boardRepository.selectByBoardId(boardId))
                .map(BoardDto::getPassword)
                .orElse("");
        String encryptedPassword = StringUtil.encrypt(password);

        return !findPassword.equals(encryptedPassword);
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

        String encryptedPwd = StringUtil.encrypt(form.getPassword());
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
