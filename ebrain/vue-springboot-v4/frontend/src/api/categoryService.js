import axios from "axios";

/**
 * 카테고리를 조회합니다.
 * @return 성공 : categories 실패 : errorMessage
 */
export const getCategories = () => {
  return axios.get('/api/categories')
      .then(({data: {data: {categories}}}) => {
        return formatCategories(categories);
      })
      .catch(({response: {data: {errorMessage}}}) => {
        console.error(errorMessage);
        throw new Error(errorMessage);
      });
}

/**
 * 카테고리를 포맷하여 그륩화 합니다.
 * @param categories
 * @returns 키 : 상위 카테고리, 값 : 하위 카테고리 배열
 */
const formatCategories = (categories) => {
  return categories.reduce((result, category) => {
    const { parentName } = category;
    if (!result[parentName]) {
      result[parentName] = [];
    }
    result[parentName].push(category);
    return result;
  }, {});
}