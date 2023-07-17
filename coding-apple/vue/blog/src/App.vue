<template>
  <img alt="Vue logo" src="./assets/logo.png">

  <router-link to="/list">리스트</router-link>
  <router-link to="/home">홈</router-link>

  <router-view :블로그글="블로그글"></router-view>

</template>
<script>
import data from '@/assets/data'
export default {
  name: 'App',
  components: {
  },
  data() {
    return {
      블로그글 : data
    }
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
