package com.study.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 게시글 등록을 위한 Form
 */
@Data
public class BoardRegisterForm {

    @NotNull(message = "카테고리는 필수 항목입니다.")
    private Long categoryId; // 카테고리 번호

    @NotBlank(message = "작성자는 필수 항목입니다.")
    @Size(min = 3, max = 4, message = "작성자는 3글자 이상 5글자 미만 이여야 합니다.")
    private String writer; // 작성자

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(min = 4, max = 99, message = "제목은 4글자 이상 100글자 미만 이여야 합니다.")
    private String title; // 제목

    @NotBlank(message = "내용은 필수 항목입니다.")
    @Size(min = 4, max = 1999, message = "내용은 4글자 이상 2000글자 미만 이여야 합니다.")
    private String content; // 내용

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "영문/숫자/특수문자를 포함 하여야 합니다.")
    private String password; // 비밀번호

    private String confirmPassword; // 확인용 비밀번호
    private List<MultipartFile> files; // 첨부파일 목록

    /**
     * 등록 폼에서 입력된 패스워드 두개의 값을 비교합니다
     * @return 입력된 패스워드가 서로 같으면 true 반환
     */
    public boolean validConfirmPassword() {
        return password.equals(confirmPassword);
    }
}
