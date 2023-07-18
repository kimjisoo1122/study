<template>

  <form class="search-form" @submit.prevent="submitForm">

    <div class="search-container">

      <div class="date-container">
        <p class="date-register">등록일</p>
        <input type="date"
               class="date-from"
               name="fromDate"
               v-model="form.fromDate">
        <span class="date-between"> ~ </span>
        <input type="date"
               class="date-to"
               name="toDate"
               v-model="form.toDate">
      </div>

      <div class="condition-container">

        <select name="searchCategory"
                class="condition-category"
                v-model="form.searchCategory">
          <option value="">전체 카테고리</option>
          <optgroup v-for="(categoryGroup, groupName) in Object.keys(categories)"
                    :key="groupName"
                    :label="categoryGroup">
            <option v-for="(category, i) in categories[categoryGroup]"
                    :key="i"
                    :value="category.categoryId">
              {{ category.categoryName }}
            </option>
          </optgroup>
        </select>

        <input type="text"
               name="search"
               class="condition-search"
               v-model="form.search"
               placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)">
        <button type="submit" class="condition-submit">검색</button>
      </div>

    </div>

  </form>

</template>

<script>

export default {
  name : "BoardSearch",
  data() {
    return {
      form : {
        fromDate : '',
        toDate : '',
        search : '',
        searchCategory : '',
      }
    }
  },
  props : {
    categories : Object
  },
  methods : {
    submitForm() {

    }
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