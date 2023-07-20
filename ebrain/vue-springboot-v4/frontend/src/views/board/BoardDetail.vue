<!-- 게시글상세 컴포넌트 입니다. -->
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
          <p>{{ board.categoryName }}</p>
          <h1 class="header-title">{{ board.title }}</h1>
        </div>
        <p class="header-view">조회수: {{ board.viewCnt }}</p>
      </div>
    </div>

    <div class="content-container">
      <p class="content-text">{{ board.content }}</p>
    </div>


    <File v-for="(file, i) in files" :key="i" :file="file"/>

    <Reply :replies="replies" @registerReply="registerReply"/>

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

  <BoardUpdate v-if="showUpdate"
               :board="board"
               :files="files"
               :condition="condition"
               @cancelUpdate="showUpdate = false"
               @updateBoard="initBoardDetail"/>

  <Transition name="fadeDeleteModal">
    <BoardDeleteModal v-if="showDeleteModal"
                      :condition="condition"
                      @cancelDelete="showDeleteModal = false"/>
  </Transition>


</template>

<script>

import {formatDate} from "@/util/formatUtil";
import {getBoardDetail} from "@/api/boardService";
import {createCondition, createSearchQuery} from "@/util/queryparamUtil";
import router from "@/router";
import Reply from "@/views/board/Reply.vue";
import File from "@/views/board/BoardFile.vue";
import BoardUpdate from "@/views/board/BoardUpdate.vue";
import BoardDeleteModal from "@/views/board/BoardDeleteModal.vue";

export default {
  name: "BoardDetail",
  components: {BoardDeleteModal, BoardUpdate, File, Reply},
  data() {
    return {
      board: {},
      files: [],
      replies: [],
      condition: {},
      showUpdate: false,
      showDeleteModal: false,
    }
  },

  methods: {
    createSearchQuery,

    initBoardDetail: function () {
      getBoardDetail(this.$route.params.boardId)
          .then(({board, files, replies}) => {
            this.formatDate(board, replies);

            this.board = board;
            this.files = files;
            this.replies = replies;
            this.showUpdate = false;
          })
          .catch(errorMessage => {
            console.error(errorMessage);
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

  created() {
    this.initBoardDetail();
  }

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
    font-size: 20px;
    font-weight: normal;
  }

  .header-title {
    font-size: 20px;
    font-weight: normal;
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