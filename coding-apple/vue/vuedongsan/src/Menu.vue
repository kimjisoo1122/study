<template>
  <div class="menu">
    <a v-for="(메뉴, i) in 메뉴들" :key="i">{{ 메뉴 }}</a>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Menu",
  props: {
    메뉴들: Array
  }
}
</script>

<style scoped>
.menu {
  background-color: darkslateblue;
  padding: 15px;
  border-radius: 5px;
}

.menu a {
  color: white;
  padding: 10px;
}
</style>