package com.study.service;

import com.study.config.MyBatisSqlSessionFactory;
import com.study.dto.CategoryDto;
import com.study.mapper.CategoryMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 카테고리를 처리하는 서비스입니다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryService {

    private static final CategoryService CATEGORY_SERVICE = new CategoryService();

    /**
     * 카테고리를 조회합니다.
     * @return 맵의 키 : 부모 카테고리 이름, 맵의 값 : 자식 카테고리
     */
    public Map<String, List<CategoryDto>> findAll() {

        Map<String, List<CategoryDto>> categories = null;
        try (SqlSession sqlSession = MyBatisSqlSessionFactory.openSession(true)) {
            CategoryMapper categoryMapper = sqlSession.getMapper(CategoryMapper.class);
            categories = categoryMapper.selectAll().stream().
                    collect(Collectors.groupingBy(CategoryDto::getParentName));
        } catch (Exception e) {
            throw new IllegalStateException("카테고리 조회에 실패하였습니다.");
        }

        return categories;
    }

    /**
     * @return CATEGORY_SERVICE 싱글톤 객체 반환
     */
    public static CategoryService getCategoryService() {
        return CATEGORY_SERVICE;
    }
}
