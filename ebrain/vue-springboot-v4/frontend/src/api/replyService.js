import axios from "axios";

/**
 * 댓글을 등록합니다.
 * @param replyDto 댓글등록정보
 * @return reply 등록된 댓글
 */
export const registerReply = (replyDto) => {
  return axios.post('/api/reply', replyDto)
      .then(({data: {data: {reply}}}) => {
        this.$emit('registerReply', reply);
        this.replyContent = '';
        return reply;
      })
      .catch(({response: {data: {errorMessage}}}) => {
        new Error(errorMessage);
      });
}