<template>


  <Modal/>
  <div class="menu">
    <a v-for="(메뉴, i) in 메뉴들" :key="i">{{ 메뉴 }}</a>
  </div>

  <Discount></Discount>

  <div v-for="(원룸, i) in 원룸들" :key="i" style="width: 30vw; margin: 0 auto">
    <img :src="원룸.image" alt="" class="room-img">
    <h4 @click="모달창열렸니 = true; 누른거 = i">{{ 원룸.title }}</h4>
    <p>{{ 원룸.price }} 만원</p>
  </div>
</template>

<script>
import data from "./assets/oneroom";
import Discount from "./Discount.vue";
import Modal from "./Modal.vue";

export default {
  name: 'App',
  data() {
    return {
      누른거: 0,
      원룸들: data,
      모달창열렸니: false,
      메뉴들: ['Home', 'Shop', 'About'],
    }
  },
  methods: {
    // increase(i) {
    //   // this.products[i].신고수++;
    // }
  },
  components: {Modal, Discount}
};
</script>

<style>
body {
  margin: 0;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.menu {
  background-color: darkslateblue;
  padding: 15px;
  border-radius: 5px;
}

.menu a {
  color: white;
  padding: 10px;
}

.room-img {
  width: 100%;
  margin-top: 40px;
  display: block;
}

.black-bg {
  position: fixed;
  padding: 20px;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.white-bg {
  width: 35%;
  margin: 50px auto;
  height: 35%;
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  position: relative;

  /*position: absolute;*/
  /*top: 50%;*/
  /*left: 50%;*/
  /*transform: translate(-50%, -50%);*/
}

.white-bg button {
  padding: 10px 15px;
  background-color: white;
  border-radius: 3px;
  border: 1px solid lightgray;
  cursor: pointer;
  position: absolute;
  right: 10px;
  bottom: 10px;
}

.white-bg-img {
  width: 50%;
  margin: 10px auto;
  border-radius: 10px;
  overflow: hidden;
}

.white-bg-img img {
  width: 100%;
  display: block;
}

.discount {
  background-color: #eee;
  padding: 10px;
  margin: 10px;
  border-radius: 5px;
}
</style>
