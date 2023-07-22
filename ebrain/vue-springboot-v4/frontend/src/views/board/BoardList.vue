<!-- 게시글목록 라우터뷰 입니다. -->
<template>

    <!-- 게시글 검색조건 -->
    <BoardSearch :condition="condition"/>

    <!-- 게시글목록 헤더 -->
    <BoardListHeader :boardCnt="boardCnt"/>

    <!-- 게시글 -->
    <Board v-for="(board, idx) in boardList"
           :key="idx"
           :board="board"
           :condition="condition"/>

    <!-- 페이징처리 -->
    <Pagination class="pagination-component"
                :condition="condition"
                :boardCnt="boardCnt"/>

    <!-- 게시글등록 -->
    <div class="board-register-container">
        <router-link class="board-register-router"
                     :to="createSearchQuery('/board/register', condition)">
          <button class="board-register-button">등록</button>
        </router-link>
    </div>

</template>

<script>

import BoardSearch from "@/components/board/BoardSearch.vue";
import Board from "@/components/board/Board.vue";
import Pagination from "@/components/board/Pagination.vue";
import BoardListHeader from "@/components/board/BoardListHeader.vue";
import {createSearchQuery} from "@/util/queryparamUtil";
import {getBoardList} from "@/api/boardService";
import {formatDate} from "@/util/formatUtil";

export default {
  name: "BoardList",

  components: {BoardListHeader, Pagination, Board, BoardSearch},

  data() {
    return {
      boardCnt: 0, // 게시글갯수
      boardList: [], // 게시글목록
      categories: {}, // 카테고리목록
      condition: {}, // 검색조건
      showRegister: false, // 게시글등록폼
    }
  },

  watch: {
    // 라우터는 같은 컴포넌트의 이동시에 created나 mounted 훅을 발생시키지 않음.
    // watch를통해 url의 변경사항을 추적한 후 변경사항이있을 경우 다시 랜더링
    '$route.query': {
      immediate: true,
      handler: 'initBoardList',
    }
  },

  mounted() {
    // 게시글삭제한 경우 로컬스토리지에서 메시지알람
    const deleteMsg = localStorage.getItem('deleteMsg');
    if (deleteMsg) {
      localStorage.removeItem('deleteMsg');
      alert(deleteMsg);
    }
  },

  methods: {
    createSearchQuery,
    /**
     * URL의 검색조건들을 파싱하여 condition 객체를 생성합니다.
     * @returns condition 검색조건
     */
    createCondition() {
      const {page, fromDate, toDate, search, searchCategory} = this.$route.query;

      return {
        page: page || 1,
        fromDate: fromDate || '',
        toDate: toDate || '',
        search: search || '',
        searchCategory: searchCategory || ''
      };
    },

    /**
     * 게시글목록을 초기생성합니다.
     */
    initBoardList() {
      const condition = this.createCondition();
      this.condition = condition;

      getBoardList(condition)
          .then(({boardList, boardCnt}) => {
            boardList.forEach(e => {
              e.createDate = formatDate(e.createDate);
              e.updateDate = formatDate(e.updateDate);
            })
            this.boardList = boardList;
            this.boardCnt = boardCnt;
          })
          .catch(({message}) => {
            console.error(message);
          })
    },
  },
}

</script>

<style scoped>

  .pagination-component {
    margin-top: 20px;
  }

  .board-register-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }

  .board-register-button {
    background-color: grey;
    border: 1px solid black;
    width: 50px;
    color: white;
    cursor: pointer;
  }
</style>