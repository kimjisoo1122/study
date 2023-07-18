<template>

  <div class="board-list-container">

    <BoardSearch :categories="categories" />

    <div class="board-list-cnt-container">
      <p class="board-list-cnt">{{ boardCnt }} 건</p>
    </div>

    <div class="board-header-container">
      <ul class="board-header-list-container">
        <li class="board-header-category">카테고리</li>
        <li class="board-header-title">제목</li>
        <li class="board-header-writer">작성자</li>
        <li class="board-header-view">조회수</li>
        <li class="board-header-create">등록 일시</li>
        <li class="board-header-update">수정 일시</li>
      </ul>
    </div>

    <Board v-for="(board, idx) in boardList" :key="idx" :board="board" :condition="condition"/>

  </div>

  <Pagination :condition="condition" :boardCnt="boardCnt"/>

</template>

<script>

import BoardSearch from "@/components/board/BoardSearch.vue";
import axios from "axios";
import Board from "@/components/board/Board.vue";
import {formatCategories, formatDate} from "@/format";
import Pagination from "@/components/board/Pagination.vue";

export default {
  name: "BoardList",
  components: {
    Pagination, Board, BoardSearch
  },
  data() {
    return {
      boardCnt: 0,
      boardList: [],
      categories: {},
      condition: {},
    }
  },
  created() {
    const { page, fromDate, toDate, search, searchCategory } = this.$route.query;
    const condition = {
      page: page || 1,
      fromDate: fromDate || '',
      toDate: toDate || '',
      search: search || '',
      searchCategory: searchCategory || ''
    };

    console.log(condition);
    this.condition = condition;

    axios.get('/api/board', { params : condition })
        .then(({ data: { data: { boardList, boardCnt, categories }}}) => {
          boardList.forEach(e => {
            e.createDate = formatDate(e.createDate);
            e.updateDate = formatDate(e.updateDate);
          })
          this.boardList = boardList;
          this.boardCnt = boardCnt;
          this.categories = formatCategories(categories);
        })
        .catch(({ response: { data: { errorMessage }}}) => {
          console.error(errorMessage);
        })
  }
}

</script>

<style scoped>

  .board-list-cnt {
    font-size: 12px;
  }

  .board-header-list-container {
    align-items: center;
    border-bottom: 2px solid black;
    border-top: 1px solid #eee;
    display: flex;
    font-weight: bold;
    height: 30px;
    list-style: none;
    margin: 0;
    padding: 0;
    font-size: 14px;
    text-align: center;
  }

  .board-header-category {
    width: 15%;
  }

  .board-header-title {
    width: 45%;
  }

  .board-header-writer {
    width: 7%;
    margin-right: 15px;
  }

  .board-header-view {
    width: 5%;
  }

  .board-header-create {
    width: 15%;
    margin-left: 10px;
  }

  .board-header-update {
    width: 15%;
  }

</style>