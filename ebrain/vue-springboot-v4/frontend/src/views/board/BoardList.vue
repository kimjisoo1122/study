<!-- 게시글목록 컴포넌트 입니다. -->
<template>

  <div class="board-list-container">

    <BoardSearch :condition="condition"/>

    <BoardListHeader :boardCnt="boardCnt"/>

    <Board v-for="(board, idx) in boardList" :key="idx" :board="board" :condition="condition"/>

    <Pagination class="pagination-component" :condition="condition" :boardCnt="boardCnt"/>

    <div class="board-register-container">
      <button class="board-register-button">
        <router-link  class="board-register-router"
                      :to="createSearchQuery('/board/register', condition.page, condition)">
          등록
        </router-link>
      </button>
    </div>

  </div>

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
import BoardSearch from "@/views/board/BoardSearch.vue";
import axios from "axios";
import Board from "@/views/board/Board.vue";
import {formatDate} from "@/util/formatUtil";
import {createPageSearchQuery} from "@/util/queryparamUtil";
import Pagination from "@/components/Pagination.vue";
import BoardListHeader from "@/views/board/BoardListHeader.vue";

export default {
  name: "BoardList",

  components: {
    BoardListHeader,
    Pagination,
    Board, BoardSearch
  },

  data() {
    return {
      boardCnt: 0, // 게시글갯수
      boardList: [], // 게시글목록
      categories: {}, // 카테고리목록
      condition: {}, // 검색조건
      showRegister: false, // 게시글등록 ON/OFF
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

  methods: {
    createSearchQuery: createPageSearchQuery,
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
      axios.get('/api/board', {params: condition})
          .then(({data: {data: {boardList, boardCnt}}}) => {
            boardList.forEach(e => {
              e.createDate = formatDate(e.createDate);
              e.updateDate = formatDate(e.updateDate);
            })
            this.boardList = boardList;
            this.boardCnt = boardCnt;
          })
          .catch(({response: {data: {errorMessage}}}) => {
            console.error(errorMessage);
          });
    },
  },

  mounted() {
    const deleteMsg = localStorage.getItem('deleteMsg');
    if (deleteMsg) {
      localStorage.removeItem('deleteMsg');
      alert(deleteMsg);
    }
  },
}

</script>

<style scoped>

  .pagination-component {
    margin-top: 10px;
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
  }

  .board-register-router {
    color: white;
    text-decoration: none;
  }

</style>