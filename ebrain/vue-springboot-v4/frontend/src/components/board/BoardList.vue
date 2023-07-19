<!-- 게시글목록 컴포넌트 입니다. -->
<template>

  <div class="board-list-container">

    <!-- 검색하면 emit으로 form(condition)정보를 넘긴다.    -->
    <BoardSearch :categories="categories" :condition="condition" />

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

  <Pagination class="pagination-component" :condition="condition" :boardCnt="boardCnt"/>

</template>


<script>
/**
 * 컴포넌트 라이프사이클
 * 1. 컴포넌트 인스턴스를 생성한다
 * 2. data(), props, methods등 컴포넌트가 가진 값들을 초기화한다.
 * 3. beforeCreate 라이프사이클 훅을 호출한다. 이때 완전한 초기화상태는 아니라 data나 props등에 접근이 불가하다.
 * 4. created 훅을 호출한다 이때 컴포넌트는 완전한 초기화가 되었으며 이때는 모든 데이터에 접근이 가능하다.
 * 5. 컴포넌트가 완전히 생성했으니 컴포넌트를 컴파일하여 가상 DOM을 생성한다.
 * 6. 이때 자식 컴포넌트한테 props가 전송되고 자식컴포넌트들도 가상 DOM을 생성한다.
 * 7. 부모,자식 컴포넌트들의 모든 초기화가 이뤄지면 실제 DOM에 마운트를한다
 * 8. 자식 -> 부모순으로 beforeMount훅이 호출되고 실제 DOM에 마운트(화면에 모두 그려짐)됐으면 mounted훅이 호출된다.
 * 9. 컴포넌트들의 data()또는 compute, props 의 값이 변경되면 재랜더링을 하는데 이떄 update 훅이 호출된다.
 * 10. 다른 컴포넌트로 라우팅되면 destroy훅이 호출된다.
 */
import BoardSearch from "@/components/board/BoardSearch.vue";
import axios from "axios";
import Board from "@/components/board/Board.vue";
import {formatCategories, formatDate} from "@/format";
import Pagination from "@/components/board/Pagination.vue";

export default {
  name: "BoardList",
  components: {
    Pagination,
    Board, BoardSearch
  },
  data() {
    return {
      boardCnt: 0, // 게시글갯수
      boardList: [], // 게시글목록
      categories: {}, // 카테고리목록
      condition: {}, // 검색조건
    }
  },

  created() {
    // 카테고리는 컴포넌트생성시에 한번만 조회합니다.
    this.getCategories();
  },
  watch: {
    // 라우터는 같은 컴포넌트의 이동시에 created나 mounted 훅을 발생시키지 않음.
    // watch를통해 url의 변경사항을 추적한 후 변경사항이있을 경우 다시 랜더링
    '$route.query': {
      immediate: true,
      handler: 'initBoardList',
    }
  },
  methods: {
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
      console.log('list init');
      const condition = this.createCondition();
      this.condition = condition;
      this.getBoardList(condition);
    },

    /**
     * BoardSearch에서 검색한 조건들로 재조회합니다.
     * @param form BoardSearchForm
     */
    searchSubmit(form) {
      form.page = this.$route.query.page;

      this.getBoardList(form);
    },

    /**
     * 게시글목록을 조회합니다.
     * @param condition 검색조건
     */
    getBoardList(condition) {
      axios.get('/api/board', { params : condition })
          .then(({ data: { data: { boardList, boardCnt }}}) => {
            boardList.forEach(e => {
              e.createDate = formatDate(e.createDate);
              e.updateDate = formatDate(e.updateDate);
            })
            this.boardList = boardList;
            this.boardCnt = boardCnt;
          })
          .catch(({ response: { data: { errorMessage }}}) => {
            console.error(errorMessage);
          })
    },
    /**
     * 카테고리를 조회합니다.
     */
    getCategories() {
      axios.get('/api/categories')
          .then(({data: {data: {categories}}}) => {
            this.categories = formatCategories(categories);
          })
          .catch(({response: {data: {errorMessage}}}) => {
            console.error(errorMessage);
          })
    },
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

  .pagination-component {
    margin-top: 10px;
  }

</style>