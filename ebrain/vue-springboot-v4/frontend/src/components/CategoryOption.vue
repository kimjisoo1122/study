<!-- 카테고리 셀렉 옵션을 제공하는 컴포넌트입니다.-->
<template>

  <option value="">전체 카테고리</option>
  <optgroup v-for="(categoryGroup, groupIdx) in Object.keys(categories)"
            :key="groupIdx"
            :label="categoryGroup">
    <option v-for="(category, idx) in categories[categoryGroup]"
            :key="idx"
            :value="category.categoryId">
      {{ category.categoryName }}
    </option>
  </optgroup>

</template>

<script>
import {getCategories} from "@/api/categoryService";
export default {
  name: "CategoryOption",
  data() {
    return {
      categories: {}
    }
  },

  created() {
    getCategories()
        .then(categories => {
          this.categories = categories
        })
        .catch(errorMessage => console.error(errorMessage));
  }

}
</script>

<style scoped>

</style>