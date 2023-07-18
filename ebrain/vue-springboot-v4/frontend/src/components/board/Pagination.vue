<template>

  <div class="paging-container" >

    <div class="paging-prev-total-container">
      <router-link class="paging-prev-total"
                   v-if="isPrevious"
                    :to="{
                      path: '/board',
                      query: {
                        page: beginPage - 1,
                        fromDate: condition.fromDate,
                        toDate: condition.toDate,
                        search: condition.search,
                        searchCategory: condition.searchCategory
                      }}">
        &lt;&lt;
      </router-link>
    </div>

    <div class="paging-prev-container">
      <router-link class="paging-prev"
                   v-if="totalCnt !== 0 && page !== beginPage"
                   :to="{
                      path: '/board',
                      query: {
                        page: page - 1,
                        fromDate: condition.fromDate,
                        toDate: condition.toDate,
                        search: condition.search,
                        searchCategory: condition.searchCategory
                      }}">
        &lt;
      </router-link>
    </div>

    <div v-if="totalCnt !== 0" class="paging-index-container">
      <router-link class="paging-page"
                   v-for="page in endPage" :key="page"
                   :to="{
                      path: '/board',
                      query: {
                        page: page,
                        fromDate: condition.fromDate,
                        toDate: condition.toDate,
                        search: condition.search,
                        searchCategory: condition.searchCategory
                      }}">
        {{ page }}
      </router-link>
    </div>

    <div class="paging-next-container">
      <router-link class="paging-next"
                   v-if="totalCnt !== 0 && page !== endPage"
                   :to="{
                      path: '/board',
                      query: {
                        page: page + 1,
                        fromDate: condition.fromDate,
                        toDate: condition.toDate,
                        search: condition.search,
                        searchCategory: condition.searchCategory
                      }}">
        &gt;
      </router-link>
    </div>

    <div class="paging-next-total-container">
      <router-link class="paging-next-total"
                   v-if="isNext"
                   :to="{
                      path: '/board',
                      query: {
                        page: maxPage + 1,
                        fromDate: condition.fromDate,
                        toDate: condition.toDate,
                        search: condition.search,
                        searchCategory: condition.searchCategory
                      }}">
        &gt;&gt;
      </router-link>
    </div>

  </div>

</template>

<script>
export default {
  name: "Pagination",
  props: {
    condition: Object,
    boardCnt: Number,
  },
  data() {
    return {
      page: Number(this.$route.query.page), // 현재페이지
      navSize: 10,// 네비게이션 사이즈
      pageSize: 10, // 페이지 사이즈
    }
  },
  computed: {
    totalCnt: function () {
      return this.boardCnt; // 게시글 총 갯수
    },
    maxPage: function (){
      return Math.ceil(this.totalCnt / this.pageSize) // 게시글 총 페이지
    },
    beginPage: function () {
      return Math.trunc((this.page - 1) / this.navSize) * this.navSize + 1; // 시작페이지
    },
    endPage: function () {
      return Math.min((this.beginPage + this.navSize - 1), this.maxPage); // 마지막페이지
    },
    isPrevious: function () {
      return this.beginPage > 1; // 이전페이지 유무
    },
    isNext: function () {
      return this.maxPage !== this.endPage; // 다음페이지 유무
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