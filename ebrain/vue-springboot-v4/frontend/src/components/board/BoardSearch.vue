<!-- 게시글 검색조건 컴포넌트 입니다. -->
<template>

  <form class="search-form" @submit.prevent="submitSearch">

    <div class="search-container">

      <div class="date-container">
        <p class="date-register">등록일</p>
        <input type="date"
               class="date-from"
               v-model="fromDate">
        <span class="date-between"> ~ </span>
        <input type="date"
               class="date-to"
               v-model="toDate">
      </div>

      <div class="condition-container">

        <select class="condition-category"
                v-model="searchCategory">
          <CategoryOption :selectedId="searchCategory"/>
        </select>

        <input type="text"
               class="condition-search"
               placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)"
               v-model="search">
        <button type="submit" class="condition-submit">검색</button>
      </div>

    </div>

  </form>

</template>

<script>
import CategoryOption from "@/components/board/CategoryOption.vue";

export default {
  name : "BoardSearch",

  components: {CategoryOption},

  data() {
    return {
      page: 1, // 페이지
      fromDate: '', // 작성일이후
      toDate: '', // 작성일이전
      search: '', // 검색어
      searchCategory: '', // 검색카테고리
    }
  },

  props: {
    condition: Object, // 검색조건
  },

  created() {
    this.initBoardSearch();
  },

  methods : {
    /**
     * 검색조건을 전송합니다.
     */
    submitSearch() {
      this.$router.push({
        path: '/board',
        query: {
          page: 1,
          fromDate: this.fromDate,
          toDate: this.toDate,
          search: this.search,
          searchCategory: this.searchCategory
        }
      });

    },

    /**
     * condition객체를 통해 검색조건을 설정합니다.
     */
    initBoardSearch() {
      this.page = this.condition.page;
      this.fromDate =  this.condition.fromDate;
      this.toDate =  this.condition.toDate;
      this.search =  this.condition.search;
      this.searchCategory =  this.condition.searchCategory;
    },

  },

}
</script>

<style scoped>

  .search-container {
    align-items: center;
    border: 1px solid black;
    display: flex;
    padding: 10px;
    height: 40px;
  }

  .date-register {
    font-size: 12px;
  }

  .date-container {
    align-items: center;
    display: flex;
    gap: 10px;
  }

  .condition-container {
    display: flex;
    flex-grow: 1;
    margin-left: 20px;
  }

  .condition-category {
    padding: 0 5px;
  }

  .condition-search {
    flex-basis: 70%;
    margin: 0 10px;
    padding: 0 5px;
  }

  .condition-submit {
    background-color: grey;
    border: 1px solid black;
    color: white;
    width: 50px;
    cursor: pointer;
  }

</style>