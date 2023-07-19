package com.study.api;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Form 데이터 유효성검증을 위한 API 응답 DTO 입니다.
 */
@Data
public class ResponseFormValidDto extends ResponseDto {

    private Map<String, String> errorFields = new HashMap<>(); // 에러필드
}
