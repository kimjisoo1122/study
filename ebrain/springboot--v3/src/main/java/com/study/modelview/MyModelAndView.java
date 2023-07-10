package com.study.modelview;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * MyModelAndView 클래스는 뷰의 논리적 이름(logicalViewPath)과
 * 해당 뷰와 함께 전달될 모델 데이터를 저장하는 역할을 합니다.
 * 웹 애플리케이션에서 뷰를 렌더링하기 위한 데이터를 저장하고
 * 제공하는 역할을 합니다.
 */
@Getter @Setter
public class MyModelAndView {
    // 뷰에 대한 논리적인 경로
    private String logicalViewPath;
    // 뷰와 함께 전달될 데이터를 저장하는 Map 객체
    private final Map<String, Object> model = new HashMap<>();
}
