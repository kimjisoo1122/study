/**
 * 게시글검색조건 라우터링크를 생성합니다.
 * @param path 이동경로
 * @param page 이동페이지
 * @param condition 검색조건
 * @returns 라우터링크
 */
export const createPageSearchQuery = (path, page, condition) => {
  return {
    path: path,
    query: {
      page,
      fromDate: condition.fromDate,
      toDate: condition.toDate,
      search: condition.search,
      searchCategory: condition.searchCategory
    }
  }
}

export const createSearchQuery = (path, condition) => {
  return createPageSearchQuery(path, condition.page, condition);
}

/**
 * 라우트쿼리로부터 검색조건을 설정합니다.
 * @param routeQuery
 */
export const createCondition = (routeQuery) => {
  const {page, fromDate, toDate, search, searchCategory} = routeQuery;
  const condition = {};
  condition.page = page || 1;
  condition.fromDate = fromDate || '';
  condition.toDate = toDate || '';
  condition.search = search || '';
  condition.searchCategory = searchCategory || '';

  return condition;
}