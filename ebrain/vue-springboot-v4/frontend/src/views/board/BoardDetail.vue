<!-- 게시글상세 라우터뷰 입니다. -->
<template>

  <div v-if="!showUpdate" class="board-container">

    <div class="header-container">

      <div class="header-top-container">
        <p class="header-name">{{ board.writer }}</p>
        <div class="header-date-container">
          <p class="header-date-create">등록일시 {{ board.createDate }}</p>
          <p class="header-date-update">수정일시 {{ board.updateDate }}</p>
        </div>
      </div>

      <div class="header-bottom-container">
        <div class="header-title-container">
          <p class="header-title-category">{{ board.categoryName }}</p>
          <h1 class="header-title">{{ board.title }}</h1>
        </div>
        <p class="header-view">조회수: {{ board.viewCnt }}</p>
      </div>
    </div>

    <div class="content-container">
      <p class="content-text">{{ board.content }}</p>
    </div>

    <!-- 게시글 첨부파일 -->
    <BoardFile v-for="(file, i) in files"
               :key="i"
               :file="file"/>

    <!-- 게시글 댓글목록, 댓글등록 -->
    <Reply :replies="replies"
           :boardId="board.boardId"
           @registerReply="registerReply"/>

    <div class="button-container">
      <router-link :to="createSearchQuery('/board', condition)">
        <button class="button-list">목록</button>
      </router-link>
      <button class="button-update" @click="showUpdate = true">수정</button>
      <button type="button"
              class="button-remove-router"
              @click="showDeleteModal = true">삭제
      </button>
    </div>

  </div>

  <!-- 게시글 업데이트  -->
  <BoardUpdate v-if="showUpdate"
               :board="board"
               :files="files"
               :condition="condition"
               @cancelUpdate="showUpdate = false"
               @updateBoard="initBoardDetail"/>

  <!-- 게시글삭제 모달  -->
  <Transition name="fadeDeleteModal">
    <BoardDeleteModal v-if="showDeleteModal"
                      :condition="condition"
                      @cancelDelete="showDeleteModal = false"/>
  </Transition>


</template>

<script>

import Reply from "@/components/Reply.vue";
import BoardFile from "@/components/board/BoardFile.vue";
import BoardUpdate from "@/components/board/BoardUpdate.vue";
import BoardDeleteModal from "@/components/board/BoardDeleteModal.vue";
import {formatDate} from "@/util/formatUtil";
import {getBoardDetail} from "@/api/boardService";
import {createCondition, createSearchQuery} from "@/util/queryparamUtil";

export default {
  name: "BoardDetail",

  components: {BoardDeleteModal, BoardUpdate, BoardFile, Reply},

  data() {
    return {
      board: {}, // 게시글
      files: [], // 첨부파일
      replies: [], // 댓글목록
      condition: {}, // 검색조건
      showUpdate: false, // 업데이트폼
      showDeleteModal: false, // 게시글삭제모달창
    }
  },

  created() {
    this.initBoardDetail();
  },

  methods: {
    createSearchQuery,

    initBoardDetail() {
      getBoardDetail(this.$route.params.boardId)
          .then(({board, files, replies}) => {
            this.formatDate(board, replies);

            this.board = board;
            this.files = files;
            this.replies = replies;
            this.showUpdate = false;
          })
          .catch(({message}) => {
            console.error(message);
          });

      this.condition = createCondition(this.$route.query);
    },

    /**
     * 게시글과 댓글들의 날짜형식을 포맷팅합니다.
     * @param board 게시글
     * @param replies 댓글목록
     */
    formatDate(board, replies) {
      board.createDate = formatDate(board.createDate);
      board.updateDate = formatDate(board.updateDate);

      replies.forEach(e => {
        e.createDate = formatDate(e.createDate);
      })
    },

    /**
     * 이벤트로 전달된 댓글을 목록에 추가합니다.
     * @param reply 등록된댓글
     */
    registerReply(reply) {
      reply.createDate = formatDate(reply.createDate);
      this.replies.push(reply);
    },
  },

}

</script>

<style scoped>

  .header-container {
    display: flex;
    flex-direction: column;
    font-size: 14px;
    justify-content: center;
    height: 100px;
    border-bottom: 2px solid black;
  }

  .header-top-container {
    display: flex;
    justify-content: space-between;
  }

  .header-title-container {
    align-items: center;
    display: flex;
  }

  .header-title-category {
    font-size: 16px;
  }

  .header-title {
    font-size: 20px;
    font-weight: bold;
    margin-left: 20px;
  }

  .header-date-container {
    display: flex;
    gap: 10px;
  }

  .header-bottom-container {
    display: flex;
    justify-content: space-between;
  }

  .content-container {
    margin: 10px 0;
    border: 1px solid black;
    padding: 10px;
  }

  .button-container {
    border-top: 1px solid black;
    padding-top: 10px;
    text-align: center;
    margin-top: 10px;
  }

  .button-list {
    background-color: grey;
    border: 1px solid black;
  }

  .button-container button {
    cursor: pointer;
    width: 70px;
    margin: 0 5px;
  }

  .delete-button-container button {
    border: 1px solid black;
    cursor: pointer;
    width: 70px;
  }

  /* Transition */
  .fadeDeleteModal-enter-from, .fadeDeleteModal-leave-to {
    opacity: 0;
  }
  .fadeDeleteModal-enter-active, .fadeDeleteModal-leave-active {
    transition: all 0.5s;
  }
  .fadeDeleteModal-enter-to, .fadeDeleteModal-leave-from {
    opacity: 1;
  }
</style>