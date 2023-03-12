<template>
  <div class="filter-item" :class="filter" :style="`background-image: url(${imageUrl})`" >
    <slot name="a"></slot>
  </div>
</template>

<script>
export default {
  name: "FilterBox",
  props: {
    imageUrl: String,
    filter: String
  }
}
</script>

<style scoped>
.filter-item {
  width: 100px;
  height: 100px;
  background-color: cornflowerblue;
  margin: 10px 10px 10px auto;
  padding: 8px;
  display: inline-block;
  color: white;
  background-size: cover;
}
</style>