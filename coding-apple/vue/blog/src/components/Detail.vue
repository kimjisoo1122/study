<template>
<div>
  {{ $route.params.id }}
  <h4>상세페이지</h4>
  <h4>{{ 블로그글[0].title }}</h4>
<!--  <router-view></router-view>-->
</div>
</template>

<script>
export default {
  name: "Detail",
  props: {
    블로그글 : Array,
    글번호 : Number
  }
}
</script>

<style scoped>

</style>