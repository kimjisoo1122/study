<template>
<!--  TODO 컨테이너가 있고 그안에만 ROUTERVIEW로 나타낸다. -->
  <div class="container">
    <router-view></router-view>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'App',
  data() {
    return {
      boardList: '',

    }
  },
  components: {
  },
  methods: {
    home() {
      axios.get('/board').then(res => console.log(res.data)).catch(err => console.error(err));
    },
  },
}
</script>

<style>

  .container {
    border: 1px solid black;
    margin: 150px auto;
    padding: 20px;
    width: 1000px;
  }

</style>
