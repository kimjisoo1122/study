<template>
  <div>
    <img :src="원룸.image" alt="방사진" class="room-img">
    <!-- 부모에게 메시지 보낼땐 $emit() -->
    <h4 @click="send">{{ 원룸.title }}</h4>
    <p>{{ 원룸.price }} 만원</p>
    <!-- v-on 은 @로 축약가능 -->
    <!--    <button @click="increase(idx)">허위매물신고</button> <span>신고수 : {{product.신고수}}</span>-->
  </div>
</template>

<script>
export default {
  name: "Card",
  props: {
    원룸: Object
  },
  methods: {
    // 메소드안에서 데이터에 접근하려면 this를 항상 붙혀야 함
    send() {
      this.$emit('openModal', this.원룸.id);
    },
  }
}
</script>

<style scoped>

  .room-img {
    width: 100px;
    margin-top: 40px;
  }

</style>