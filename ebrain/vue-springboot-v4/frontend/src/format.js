/**
 * 카테고리를 포맷하여 상위 카테고리로 그륩화 합니다.
 * @param categories
 * @returns 키 : 상위 카테고리, 값 : 하위 카테고리 배열
 */
export const formatCategories = (categories) => {
  return categories.reduce((result, category) => {
    const {parentName} = category;
    if (!result[parentName]) {
      result[parentName] = [];
    }
    result[parentName].push(category);
    return result;
  }, {});
}

/**
 * 자바 LocalDateTime 형식의 문자열을 yyyy.MM.dd HH:mm 포맷으로 변경
 * @param date
 * @returns yyyy.MM.dd HH:mm 포맷
 */
export const formatDate = (date) => {
  return date.replace('T', ' ').replaceAll('-', '.').slice(0, -3);
}


