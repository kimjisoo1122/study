<template>
  <Modal @closeModal="모달창열렸니 = false" :원룸들="원룸들" :누른거="누른거" :모달창열렸니="모달창열렸니"/>
  <Menu :메뉴들="메뉴들"/>
  <Discount v-if="showDiscount" :showDiscount="showDiscount" @close="showDiscount == false"/>
  <button @click="카드정렬">가격순정렬</button>
  <button @click="sortBack">되돌리기</button>
  <Card @openModal="모달창열렸니 = true; 누른거 = $event" v-for="원룸 in 원룸들" :key="원룸.id" :원룸="원룸"/>
</template>

<script>
import data from "./assets/oneroom";
import Discount from "./Discount.vue";
import Modal from "./Modal.vue";
import Menu from "./Menu.vue";
import Card from "./Card.vue";

export default {
  name: 'App',
  data() {
    return {
      showDiscount: true,
      orderFlag: true,
      누른거: 0,
      원룸들오리지널: [...data],
      원룸들: data,
      모달창열렸니: false,
      메뉴들: ['Home', 'Shop', 'About'],
    }
  },
  methods: {
    카드정렬() {
      if (this.orderFlag) {
        this.원룸들.sort((a, b) => a.price - b.price);
        this.orderFlag = false;
      } else {
        this.원룸들.sort((a, b) => b.price - a.price);
        this.orderFlag = true;
      }

    },
    sortBack() {
      this.원룸들 = [...this.원룸들오리지널];
    },


  },

  // 데이터만 있을때
  created() {
  },

  components: {Modal, Discount, Menu, Card}
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

</style>
