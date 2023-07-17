<template>
  <button @click="home">테스트버튼</button>
</template>

<script>
import axios from "axios";

export default {
  name: 'App',
  components: {
  },
  methods: {
    home() {
      axios.get('/board').then(res => console.log(res.data)).catch(err => console.error(err));
    },
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
