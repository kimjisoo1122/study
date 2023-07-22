<!-- 댓글 컴포넌트입니다. -->
<template>
  <div class="reply-container">

    <!-- 댓글목록 -->
    <div class="reply-list-container">
      <div v-for="(reply, idx) in replies"
           :key="idx"
           class="reply">
        <div class="reply-date">{{ reply.createDate }}</div>
        <div class="reply-content">{{ reply.replyContent }}</div>
      </div>
    </div>

    <!-- 댓글등록 -->
    <div class="reply-register-container">
      <input type="text"
             class="reply-register-input"
             :class="replyError"
             @input="validateReply"
             v-model="replyContent"
             :placeholder="replyPlaceHolder">
      <button
          type="button"
          class="reply-register-button"
          @click="submitReply">등록
      </button>
    </div>

  </div>
</template>

<script>
import {registerReply} from "@/api/replyService";

export default {
  name: "Reply",

  data() {
    return {
      replyContent: '', // 댓글내용
      replyError: '', // 댓글에러
      replyPlaceHolder: '댓글을 입력해 주세요.',
    }
  },

  props: {
    boardId: Number,
    replies: Array,
  },

  methods: {

    /**
     * 댓글을 등록하고 등록된댓글을 게시글상세 컴포넌트에 전송합니다.
     */
    submitReply() {
      if (this.validateReply()) {
        const replyDto = {
          boardId: this.boardId,
          replyContent: this.replyContent,
        };

        registerReply(replyDto)
            .then(reply => {
              this.$emit('registerReply', reply);
              this.replyContent = '';
            })
            .catch(({message}) => {
              console.error(message);
            });
      }
    },

    /**
     * 빈 댓글을 검증합니다.
     * @returns {boolean}
     */
    validateReply() {
      if (!this.replyContent) {
        this.replyError = 'reply-error';
        this.replyPlaceHolder = '빈 댓글은 등록할수없습니다.'

        return false;
      } else {
        this.replyError = '';
        this.replyPlaceHolder = '댓글을 입력해 주세요.';

        return true;
      }
    },
  }
}
</script>

<style scoped>

  .reply-container {
    background-color: antiquewhite;
    padding: 10px;
  }

  .reply {
    border-bottom: 1px solid black;
    padding: 10px 0;
  }

  .reply-date {
    font-size: 12px;
  }

  .reply-content {
    font-size: 14px;
  }

  .reply-register-container {
    height: 100px;
  }

  .reply-register-input {
    box-sizing: border-box;
    font-size: 12px;
    height: 50px;
    margin-top: 10px;
    padding: 0 10px;
    width: 93%;
  }

  .reply-register-input::placeholder {
    color: #ccc;
  }

  .reply-register-button {
    background-color: white;
    border: 1px solid black;
    cursor: pointer;
    height: 50px;
    width: 50px;
    margin-left: 10px;
  }

  .reply-error {
    border: 1px solid red;
    outline: red;
  }

  .reply-error::placeholder {
    color: red;
  }

</style>