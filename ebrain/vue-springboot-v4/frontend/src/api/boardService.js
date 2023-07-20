import axios from "axios";
import {formatDate} from "@/util/formatUtil";

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
 * 게시글목록을 조회합니다.
 * @param condition 검색조건
 * @return boardList 게시글목록, boardCnt 게시글갯수
 */
export const getBoardList = (condition) => {
  return axios.get('/api/board', {params: condition})
      .then(({data: {data: {boardList, boardCnt}}}) => {
        return {
          boardList: boardList,
          boardCnt: boardCnt
        }
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

/**
 * 게시글 삭제를 요청합니다.
 * @param boardId 게시글번호
 * @param deletePassword 게시글비밀번호
 * @return boardId
 */
export const deleteBoard = (boardId, deletePassword) => {
  return axios.delete(`/api/board/${boardId}`, {
        data: {
          // 객체로 전달
          deletePassword: deletePassword
        }
      })
      .then(() => {
        return boardId;
      })
      .catch(({response: {data: {errorMessage}}}) => {
        throw new Error(errorMessage);
      });
}

