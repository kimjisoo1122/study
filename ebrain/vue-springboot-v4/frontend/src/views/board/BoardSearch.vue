<!-- 게시글 검색조건 컴포넌트 입니다. -->
<template>

  <form class="search-form" @submit.prevent="searchSubmit">

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
          <CategoryOption :categories="categories"/>
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
import CategoryOption from "@/components/CategoryOption.vue";

export default {
  name : "BoardSearch",
  components: {CategoryOption},
  data() {
    return {
      page: 1,
      fromDate: '',
      toDate: '',
      search: '',
      searchCategory: '',
    }
  },
  props: {
    categories: Object,
    condition: Object,
  },

  created() {
    this.setCondition();
  },

  methods : {
    /**
     * 검색조건을 전송합니다.
     */
    searchSubmit() {
      this.$router.push({
        path: '/board',
        query: {
          page: this.$route.query.page | 1,
          fromDate: this.fromDate,
          toDate: this.toDate,
          search: this.search,
          searchCategory: this.searchCategory
        }
      }).then(() => this.setCondition());

    },

    /**
     * URL을 파싱하여 검색조건을 설정합니다.
     */
    setCondition() {
      const {page, fromDate, toDate, search, searchCategory} = this.$route.query;
      this.page = page || 1;
      this.fromDate = fromDate || '';
      this.toDate = toDate || '';
      this.search = search || '';
      this.searchCategory = searchCategory || '';
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