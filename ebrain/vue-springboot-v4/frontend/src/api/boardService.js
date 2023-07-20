import axios from "axios";

const multipartConfig = {
  headers: {
    'Content-Type': 'multipart/form-data'
  },
}

/**
 * 게시글을 등록합니다.
 * @param formData 게시글등록폼 정보
 * @return boardId
 */
export const registerBoard = (formData) => {
  return axios.post('/api/board', formData, multipartConfig)
      .then(({data: {data: {boardId}}}) => {
        return boardId;
      }).catch(({response: {data: {errorFields}}}) => {
        throw new Error(errorFields);
      });
}

/**
 * 게시글을 조회합니다.
 * @param boardId 게시글번호
 * @return board 게시글, files 첨부파일, replies 댓글목록
 */
export const getBoardDetail = boardId => {
  return axios.get(`/api/board/${boardId}`)
      .then(({data: {data: {board, files, replies}}}) => {
        return {
          board: board,
          files: files,
          replies: replies
        };
      })
      .catch(({response: {data: {errorMessage}}}) => {
        throw new Error(errorMessage);
      });
}

/**
 * 게시글을 업데이트합니다.
 * @param boardId 게시글번호
 * @param formData 업데이트폼 정보
 * @return boardId
 */
export const updateBoard = (boardId, formData) => {
  return axios.put(`/api/board/${boardId}`, formData, multipartConfig)
      .then(() => {
        return boardId;
      })
      .catch(({response: {data: {errorMessage}}}) => {
        throw new Error(errorMessage);
      });
}

