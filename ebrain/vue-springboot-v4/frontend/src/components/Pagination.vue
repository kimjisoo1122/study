<!-- 게시글목록을 페이징처리하는 컴포넌트입니다. -->
<template>

  <div class="paging-container" >

    <!-- 이전 전체페이지 -->
    <div class="paging-prev-total-container">
      <router-link class="paging-prev-total"
                   v-if="isPreviousTotal"
                   :to="createPageQuery(beginPage - 1)">
        &lt;&lt;
      </router-link>
    </div>

    <!-- 이전 페이지 -->
    <div class="paging-prev-container">
      <router-link class="paging-prev"
                   v-if="isPrevious"
                   :to="createPageQuery(page - 1)">
        &lt;
      </router-link>
    </div>

    <!-- 페이지 -->
    <div v-if="totalCnt !== 0" class="paging-index-container">
      <router-link class="paging-page"
                   v-for="page in endPage" :key="page"
                   :to="createPageQuery(page)">
        {{ page }}
      </router-link>
    </div>

    <!-- 다음 페이지 -->
    <div class="paging-next-container">
      <router-link class="paging-next"
                   v-if="isNext"
                   :to="createPageQuery(page + 1)">
        &gt;
      </router-link>
    </div>

    <!-- 다음 전체페이지 -->
    <div class="paging-next-total-container">
      <router-link class="paging-next-total"
                   v-if="isNextTotal"
                   :to="createPageQuery(maxPage + 1)">
        &gt;&gt;
      </router-link>
    </div>

  </div>

</template>

<script>
import {createPageSearchQuery} from "@/util/queryparamUtil";
export default {
  name: "Pagination",

  data() {
    return {
      navSize: 10,// 네비게이션 사이즈
      pageSize: 10, // 페이지 사이즈
    }
  },

  props: {
    condition: Object, // 검색조건
    boardCnt: Number, // 게시글갯수
  },

  methods: {
    /**
     * 게시글목록 라우터링크를 생성합니다.
     * @param page 이동페이지
     * @returns {라우터링크}
     */
    createPageQuery(page) {
      return createPageSearchQuery('/board', page, this.condition);
    },
  },

  // 컴퓨티드는 계산이 필요한 값일때 사용한다
  // 또한 props또는 data가 계속 랜더링 될때마다 새로운 값을 계산할때 사용한다.
  computed: {
    page() {
      return Number(this.condition.page);
    },
    totalCnt() {
      return this.boardCnt; // 게시글 총 갯수
    },
    maxPage(){
      return Math.ceil(this.totalCnt / this.pageSize); // 게시글 총 페이지
    },
    beginPage() {
      return Math.trunc((this.page - 1) / this.navSize) * this.navSize + 1; // 시작페이지
    },
    endPage() {
      return Math.min((this.beginPage + this.navSize - 1), this.maxPage); // 마지막페이지
    },
    isPrevious() {
      return this.totalCnt !== 0 && this.page !== this.beginPage; // 이전페이지 유무
    },
    isNext() {
      return this.totalCnt !== 0 && this.page !== this.endPage; // 다음페이지 유무
    },
    isPreviousTotal() {
      return this.beginPage > 1; // 이전 전체페이지 유무
    },
    isNextTotal() {
      return this.maxPage !== this.endPage; // 다음 전체페이지 유무
    },
  }

}

</script>

<style scoped>

  .paging-container {
    align-items: center;
    display: flex;
    justify-content: center;
    gap: 5px;
  }

  .paging-container a {
    color: black;
    font-size: 12px;
    text-decoration: none;
  }

  .paging-prev, .paging-next, .paging-prev-total, .paging-next-total {
    display: flex;
    align-items: center;
    justify-content: center;
    padding-bottom: 2px;
  }


  .paging-prev-total-container,.paging-prev-container,
  .paging-next-container, .paging-next-total-container {
    width: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .paging-index-container {
    display: flex;
    justify-content: center;
    gap: 5px;
  }

</style>